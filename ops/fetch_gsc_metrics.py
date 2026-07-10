#!/usr/bin/env python3
"""Fetch the latest complete 28-day page metrics from Google Search Console."""

import argparse
import csv
from datetime import date, datetime, timedelta, timezone
from pathlib import Path
from urllib.parse import urlparse

from google.oauth2 import service_account
from googleapiclient.discovery import build


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--credentials", required=True)
    parser.add_argument("--property", default="sc-domain:oiltankroute.com")
    parser.add_argument("--output", default="output/gsc-route-metrics.csv")
    args = parser.parse_args()

    end_date = date.today() - timedelta(days=3)
    start_date = end_date - timedelta(days=27)
    credentials = service_account.Credentials.from_service_account_file(
        args.credentials,
        scopes=["https://www.googleapis.com/auth/webmasters.readonly"],
    )
    service = build("searchconsole", "v1", credentials=credentials, cache_discovery=False)
    response = service.searchanalytics().query(
        siteUrl=args.property,
        body={
            "startDate": start_date.isoformat(),
            "endDate": end_date.isoformat(),
            "dimensions": ["page"],
            "rowLimit": 25000,
            "dataState": "final",
        },
    ).execute()

    output = Path(args.output)
    output.parent.mkdir(parents=True, exist_ok=True)
    fetched_at = datetime.now(timezone.utc).isoformat()
    with output.open("w", newline="", encoding="utf-8") as handle:
        writer = csv.DictWriter(handle, fieldnames=[
            "page_path", "window_start", "window_end", "clicks", "impressions", "ctr", "position", "fetched_at"
        ])
        writer.writeheader()
        for row in response.get("rows", []):
            page_path = urlparse(row["keys"][0]).path or "/"
            impressions = int(row.get("impressions", 0))
            clicks = int(row.get("clicks", 0))
            writer.writerow({
                "page_path": page_path,
                "window_start": start_date.isoformat(),
                "window_end": end_date.isoformat(),
                "clicks": clicks,
                "impressions": impressions,
                "ctr": clicks / impressions if impressions else 0,
                "position": row.get("position", 0),
                "fetched_at": fetched_at,
            })


if __name__ == "__main__":
    main()
