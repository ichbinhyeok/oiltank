#!/usr/bin/env python3
"""Build reproducible NY county aggregates from the official NYSDEC Socrata dataset."""

import argparse
import json
from datetime import date, timedelta
from pathlib import Path
from urllib.parse import urlencode
from urllib.request import urlopen


DATASET_URL = "https://data.ny.gov/resource/u44d-k5fk.json"
SOURCE_PAGE = "https://data.ny.gov/Energy-Environment/Spill-Incidents/u44d-k5fk"


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--output", default="output/new-york-counties.json")
    parser.add_argument("--window-start", default="2024-01-01")
    args = parser.parse_args()

    window_end = date.today() - timedelta(days=1)
    where = (
        f"spill_date between '{args.window_start}T00:00:00' and '{window_end.isoformat()}T23:59:59' "
        "and source='Private Dwelling' and upper(material_name) like '%FUEL OIL%'"
    )
    query = urlencode({
        "$select": "county,count(*) as incident_count",
        "$where": where,
        "$group": "county",
        "$order": "incident_count desc",
        "$limit": "100",
    })
    with urlopen(f"{DATASET_URL}?{query}", timeout=30) as response:
        source_rows = json.load(response)

    rows = []
    for rank, row in enumerate(source_rows, start=1):
        county = row.get("county", "").strip()
        if not county:
            continue
        rows.append({
            "stateSlug": "new-york",
            "countySlug": county.lower().replace(" ", "-"),
            "countyName": county,
            "windowStart": args.window_start,
            "windowEnd": window_end.isoformat(),
            "incidentCount": int(row["incident_count"]),
            "stateRank": rank,
            "sourceTitle": "NYSDEC Spill Incidents",
            "sourceUrl": SOURCE_PAGE,
            "updatedOn": date.today().isoformat(),
        })

    output = Path(args.output)
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(rows, indent=2) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()
