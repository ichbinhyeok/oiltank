import csv
import json
import re
import time
import urllib.parse
import urllib.request
import xml.etree.ElementTree as ET
from collections import Counter
from concurrent.futures import ThreadPoolExecutor, as_completed
from html.parser import HTMLParser
from pathlib import Path


ROOT = Path(__file__).resolve().parent
UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/138 Safari/537.36"


def fetch_json(url):
    req = urllib.request.Request(url, headers={"User-Agent": UA})
    with urllib.request.urlopen(req, timeout=25) as response:
        return json.load(response)


def fetch_text(url):
    req = urllib.request.Request(url, headers={"User-Agent": UA, "Accept-Language": "en-US,en;q=0.9"})
    with urllib.request.urlopen(req, timeout=25) as response:
        return response.read().decode("utf-8", errors="replace")


def census_state_market():
    # Census Reporter republishes the Census ACS API and does not require the
    # API key that api.census.gov began requiring in this environment.
    query = urllib.parse.urlencode({
        "table_ids": "B25040,B25034",
        "geo_ids": "040|01000US",
    })
    payload = fetch_json(f"https://api.censusreporter.org/1.0/data/show/latest?{query}")
    rows = []
    for geo_id, tables in payload["data"].items():
        record_40 = tables["B25040"]["estimate"]
        record_34 = tables["B25034"]["estimate"]
        occupied = int(record_40["B25040001"])
        fuel_oil = int(record_40["B25040005"])
        housing_total = int(record_34["B25034001"])
        pre_1980 = sum(int(record_34[f"B25034{i:03d}"]) for i in range(7, 12))
        rows.append({
            "state": payload["geography"][geo_id]["name"],
            "state_fips": geo_id[-2:],
            "occupied_housing_units": occupied,
            "fuel_oil_households": fuel_oil,
            "fuel_oil_share_pct": round(100 * fuel_oil / occupied, 2),
            "housing_units": housing_total,
            "pre_1980_units": pre_1980,
            "pre_1980_share_pct": round(100 * pre_1980 / housing_total, 2),
        })
    rows.sort(key=lambda r: r["fuel_oil_households"], reverse=True)
    for rank, row in enumerate(rows, 1):
        row["fuel_oil_household_rank"] = rank
    return rows, payload.get("release", {})


STATES = [
    "new jersey", "new york", "connecticut", "maine", "massachusetts",
    "pennsylvania", "rhode island", "new hampshire", "vermont",
]

INTENTS = [
    "buried oil tank", "oil tank sweep", "underground oil tank removal",
    "abandoned oil tank records", "buying house with oil tank",
    "selling house with oil tank", "oil tank leak cleanup", "oil tank removal cost",
]


def autocomplete_one(seed):
    query = urllib.parse.urlencode({"client": "firefox", "hl": "en", "q": seed})
    try:
        data = fetch_json(f"https://suggestqueries.google.com/complete/search?{query}")
        return seed, [str(x).strip().lower() for x in data[1] if str(x).strip()]
    except Exception as exc:
        return seed, {"error": str(exc)}


def autocomplete_market():
    seeds = sorted(set(INTENTS + [f"{intent} {state}" for state in STATES for intent in INTENTS]))
    results = []
    with ThreadPoolExecutor(max_workers=6) as pool:
        futures = {pool.submit(autocomplete_one, seed): seed for seed in seeds}
        for future in as_completed(futures):
            seed, suggestions = future.result()
            if isinstance(suggestions, list):
                for rank, suggestion in enumerate(suggestions, 1):
                    results.append({"seed": seed, "rank": rank, "suggestion": suggestion})
    results.sort(key=lambda r: (r["seed"], r["rank"]))
    return results


SERP_QUERIES = [
    f"{intent} {state}"
    for state in STATES
    for intent in ["oil tank sweep", "buried oil tank home sale", "abandoned oil tank records", "oil tank removal cost"]
]


def bing_rss_one(query):
    params = urllib.parse.urlencode({"q": query, "format": "rss", "count": 10, "setlang": "en-us"})
    try:
        xml = fetch_text(f"https://www.bing.com/search?{params}")
        root = ET.fromstring(xml)
        rows = []
        for rank, item in enumerate(root.findall("./channel/item"), 1):
            link = item.findtext("link", default="")
            domain = urllib.parse.urlparse(link).netloc.lower().removeprefix("www.")
            rows.append({
                "query": query,
                "rank": rank,
                "title": item.findtext("title", default=""),
                "url": link,
                "domain": domain,
            })
        return rows
    except Exception as exc:
        return [{"query": query, "rank": 0, "title": "ERROR", "url": str(exc), "domain": ""}]


def bing_serps():
    rows = []
    with ThreadPoolExecutor(max_workers=4) as pool:
        futures = [pool.submit(bing_rss_one, query) for query in SERP_QUERIES]
        for future in as_completed(futures):
            rows.extend(future.result())
    rows.sort(key=lambda r: (r["query"], r["rank"]))
    return rows


class SeoParser(HTMLParser):
    def __init__(self):
        super().__init__()
        self.title = []
        self.h1 = []
        self.text = []
        self.links = []
        self.canonical = ""
        self.description = ""
        self.robots = ""
        self.json_ld = 0
        self._in_title = False
        self._in_h1 = False
        self._skip_depth = 0

    def handle_starttag(self, tag, attrs):
        attrs = dict(attrs)
        if tag in {"script", "style", "noscript"}:
            self._skip_depth += 1
        if tag == "title":
            self._in_title = True
        if tag == "h1":
            self._in_h1 = True
        if tag == "a" and attrs.get("href"):
            self.links.append(attrs["href"])
        if tag == "link" and attrs.get("rel", "").lower() == "canonical":
            self.canonical = attrs.get("href", "")
        if tag == "meta" and attrs.get("name", "").lower() == "description":
            self.description = attrs.get("content", "")
        if tag == "meta" and attrs.get("name", "").lower() == "robots":
            self.robots = attrs.get("content", "")
        if tag == "script" and attrs.get("type", "").lower() == "application/ld+json":
            self.json_ld += 1

    def handle_endtag(self, tag):
        if tag in {"script", "style", "noscript"} and self._skip_depth:
            self._skip_depth -= 1
        if tag == "title":
            self._in_title = False
        if tag == "h1":
            self._in_h1 = False

    def handle_data(self, data):
        clean = " ".join(data.split())
        if not clean:
            return
        if self._in_title:
            self.title.append(clean)
        if self._in_h1:
            self.h1.append(clean)
        if not self._skip_depth:
            self.text.append(clean)


def crawl_one(item):
    url, lastmod = item
    try:
        html = fetch_text(url)
        parser = SeoParser()
        parser.feed(html)
        host = urllib.parse.urlparse(url).netloc
        internal = 0
        for link in parser.links:
            parsed = urllib.parse.urlparse(urllib.parse.urljoin(url, link))
            if parsed.netloc == host:
                internal += 1
        words = re.findall(r"[A-Za-z0-9']+", " ".join(parser.text))
        result = {
            "url": url,
            "lastmod": lastmod,
            "status": 200,
            "title": " ".join(parser.title),
            "title_length": len(" ".join(parser.title)),
            "description_length": len(parser.description),
            "h1": " ".join(parser.h1),
            "word_count": len(words),
            "canonical": parser.canonical,
            "robots": parser.robots,
            "json_ld_blocks": parser.json_ld,
            "internal_links": internal,
        }
        tokens = [w.lower() for w in words]
        result["_shingles"] = set(tuple(tokens[i:i + 5]) for i in range(max(0, len(tokens) - 4)))
        return result
    except Exception as exc:
        return {"url": url, "lastmod": lastmod, "status": 0, "title": str(exc), "title_length": 0,
                "description_length": 0, "h1": "", "word_count": 0, "canonical": "", "robots": "",
                "json_ld_blocks": 0, "internal_links": 0}


def crawl_site():
    xml = fetch_text("https://oiltankroute.com/sitemap.xml")
    root = ET.fromstring(xml)
    ns = {"sm": "http://www.sitemaps.org/schemas/sitemap/0.9"}
    items = []
    for node in root.findall("sm:url", ns):
        items.append((node.findtext("sm:loc", default="", namespaces=ns),
                      node.findtext("sm:lastmod", default="", namespaces=ns)))
    with ThreadPoolExecutor(max_workers=6) as pool:
        rows = list(pool.map(crawl_one, items))
    for row in rows:
        path = urllib.parse.urlparse(row["url"]).path.strip("/")
        route_family = path.split("/")[-1] if path.count("/") >= 2 and path.startswith("states/") else ""
        peers = []
        if route_family in {"buyer-seller", "records-and-proof", "sweep-and-locate"}:
            for other in rows:
                other_path = urllib.parse.urlparse(other["url"]).path.strip("/")
                if other is not row and other_path.endswith("/" + route_family):
                    a, b = row.get("_shingles", set()), other.get("_shingles", set())
                    if a and b:
                        peers.append(len(a & b) / len(a | b))
        row["max_same_family_similarity_pct"] = round(100 * max(peers), 1) if peers else ""
    for row in rows:
        row.pop("_shingles", None)
    rows.sort(key=lambda r: r["url"])
    return rows


def write_csv(path, rows):
    if not rows:
        return
    with path.open("w", newline="", encoding="utf-8-sig") as f:
        writer = csv.DictWriter(f, fieldnames=list(rows[0].keys()))
        writer.writeheader()
        writer.writerows(rows)


def classify_suggestion(text):
    rules = [
        ("Leak, cleanup & insurance", ["leak", "cleanup", "remediation", "spill", "insurance"]),
        ("Transaction & property risk", ["buying", "selling", "house", "property"]),
        ("Sweep & inspection", ["sweep", "inspection", "scan", "locate"]),
        ("Removal & replacement", ["removal", "remove", "removing", "replace", "replacement"]),
        ("Cost & price", ["cost", "price", "how much", "worth", "free"]),
        ("Rules & records", ["law", "legal", "records", "have to", "when did", "covered"]),
    ]
    for label, needles in rules:
        if any(needle in text for needle in needles):
            return label
    return "General education"


THEMES = [
    ("Cost & price", ["cost", "price", "how much", "worth", "free"]),
    ("Removal & replacement", ["removal", "remove", "removing", "replace", "replacement"]),
    ("Sweep & inspection", ["sweep", "inspection", "scan", "locate"]),
    ("Leak, cleanup & insurance", ["leak", "cleanup", "remediation", "spill", "insurance", "covered"]),
    ("Transaction & property", ["buying", "selling", "house", "property"]),
    ("Rules & records", ["law", "legal", "records", "have to", "when did"]),
]


def main():
    ROOT.mkdir(parents=True, exist_ok=True)
    states, census_release = census_state_market()
    autocomplete = autocomplete_market()
    serps = bing_serps()
    crawl = crawl_site()
    write_csv(ROOT / "state_market_2024_acs.csv", states)
    write_csv(ROOT / "google_autocomplete_2026-07-10.csv", autocomplete)
    write_csv(ROOT / "bing_serps_2026-07-10.csv", serps)
    write_csv(ROOT / "site_crawl_2026-07-10.csv", crawl)

    suggestion_counts = Counter(r["suggestion"] for r in autocomplete)
    unique_suggestions = sorted(suggestion_counts)
    intent_counts = Counter(classify_suggestion(s) for s in unique_suggestions)
    intent_rows = [
        {"intent": intent, "unique_suggestions": count, "share_pct": round(100 * count / len(unique_suggestions), 1)}
        for intent, count in intent_counts.most_common()
    ]
    write_csv(ROOT / "autocomplete_intent_summary_2026-07-10.csv", intent_rows)
    theme_rows = []
    for theme, needles in THEMES:
        count = sum(1 for s in unique_suggestions if any(n in s for n in needles))
        theme_rows.append({"theme": theme, "matching_suggestions": count,
                           "share_of_unique_suggestions_pct": round(100 * count / len(unique_suggestions), 1)})
    theme_rows.sort(key=lambda r: r["matching_suggestions"], reverse=True)
    write_csv(ROOT / "autocomplete_theme_overlap_2026-07-10.csv", theme_rows)
    domain_counts = Counter(r["domain"] for r in serps if r["rank"] > 0 and r["domain"])
    summary = {
        "generated_at": "2026-07-10",
        "census_release": census_release,
        "census_states": len(states),
        "autocomplete_seeds": len(set(r["seed"] for r in autocomplete)),
        "autocomplete_rows": len(autocomplete),
        "unique_suggestions": len(suggestion_counts),
        "autocomplete_intent_counts": intent_counts.most_common(),
        "serp_queries": len(set(r["query"] for r in serps)),
        "serp_rows": len([r for r in serps if r["rank"] > 0]),
        "unique_serp_domains": len(domain_counts),
        "oiltankroute_serp_hits": sum(1 for r in serps if r["domain"] == "oiltankroute.com"),
        "site_urls_crawled": len(crawl),
        "site_urls_ok": sum(1 for r in crawl if r["status"] == 200),
        "site_urls_noindex": sum(1 for r in crawl if "noindex" in r["robots"].lower()),
        "site_avg_word_count": round(sum(r["word_count"] for r in crawl) / max(1, len(crawl))),
        "site_missing_canonical": sum(1 for r in crawl if not r["canonical"]),
        "site_missing_h1": sum(1 for r in crawl if not r["h1"]),
        "top_serp_domains": domain_counts.most_common(25),
        "top_repeated_suggestions": suggestion_counts.most_common(25),
    }
    (ROOT / "research_summary.json").write_text(json.dumps(summary, indent=2), encoding="utf-8")
    print(json.dumps(summary, indent=2))


if __name__ == "__main__":
    main()
