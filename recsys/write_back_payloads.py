"""Safe, reviewable write-back of recommendation payloads to Firestore.

Closes the pipeline cycle described in the thesis (Firestore export ->
offline training -> payload write-back) without adding any dependency:
the tool PATCHes ``users/{docId}`` documents through the Firestore REST API,
updating ONLY the three recommendation fields via an explicit updateMask --
it can never touch profile data, rooms, or messages.

Safety model:
  * dry-run by default -- prints exactly what would be written; nothing is
    sent until ``--apply`` is given;
  * an OAuth2 access token must be supplied explicitly (env var
    ``FIRESTORE_OAUTH_TOKEN`` or ``--token-file``); the tool never stores
    credentials;
  * per-document failures are reported and do not abort the run.

Getting a token (either works):
  * ``gcloud auth login`` with the project owner account, then
    ``gcloud auth print-access-token``; or
  * a service-account access token minted by any standard tooling.

Usage:
    python3 -m recsys.write_back_payloads \
        --payloads out/graphsage_firestore_payloads.json \
        --project fhchatroom-ba-rafiee-c58db              # dry-run (default)

    python3 -m recsys.write_back_payloads ... --apply     # actually write
"""

from __future__ import annotations

import argparse
import json
import os
import sys
import urllib.error
import urllib.parse
import urllib.request
from pathlib import Path

ALLOWED_FIELDS = ["recommendedRoomIds", "recommendationsUpdatedAt", "recommendationSource"]


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--payloads", required=True, help="JSON file produced by the pipeline")
    parser.add_argument("--project", required=True, help="Firebase project id")
    parser.add_argument("--token-file", help="file containing an OAuth2 access token")
    parser.add_argument("--apply", action="store_true", help="actually write (default: dry-run)")
    parser.add_argument("--limit", type=int, default=0, help="write at most N documents (0 = all)")
    return parser.parse_args()


def load_token(args: argparse.Namespace) -> str:
    if args.token_file:
        return Path(args.token_file).read_text(encoding="utf-8").strip()
    token = os.environ.get("FIRESTORE_OAUTH_TOKEN", "").strip()
    if not token:
        sys.exit(
            "No credentials: set FIRESTORE_OAUTH_TOKEN or pass --token-file.\n"
            "Hint: gcloud auth print-access-token"
        )
    return token


def to_firestore_fields(payload: dict) -> dict:
    return {
        "recommendedRoomIds": {
            "arrayValue": {
                "values": [{"stringValue": str(room_id)} for room_id in payload["recommendedRoomIds"]]
            }
        },
        "recommendationsUpdatedAt": {"integerValue": str(int(payload["recommendationsUpdatedAt"]))},
        "recommendationSource": {"stringValue": str(payload["recommendationSource"])},
    }


def main() -> None:
    args = parse_args()
    payloads: dict[str, dict] = json.loads(Path(args.payloads).read_text(encoding="utf-8"))
    items = sorted(payloads.items())
    if args.limit:
        items = items[: args.limit]

    print(f"{len(items)} user document(s) from {args.payloads}")
    for doc_id, payload in items[:10]:
        rooms = payload.get("recommendedRoomIds", [])
        print(f"  users/{doc_id}: {len(rooms)} rooms, source={payload.get('recommendationSource')}")
    if len(items) > 10:
        print(f"  ... and {len(items) - 10} more")

    if not args.apply:
        print("\nDRY RUN -- nothing written. Re-run with --apply to write these fields:")
        print(f"  updateMask = {ALLOWED_FIELDS}")
        return

    token = load_token(args)
    base = (
        f"https://firestore.googleapis.com/v1/projects/{args.project}"
        f"/databases/(default)/documents/users"
    )
    mask = "&".join(f"updateMask.fieldPaths={field}" for field in ALLOWED_FIELDS)
    written = failed = 0
    for doc_id, payload in items:
        url = f"{base}/{urllib.parse.quote(doc_id, safe='')}?{mask}"
        body = json.dumps({"fields": to_firestore_fields(payload)}).encode("utf-8")
        request = urllib.request.Request(
            url,
            data=body,
            method="PATCH",
            headers={"Authorization": f"Bearer {token}", "Content-Type": "application/json"},
        )
        try:
            urllib.request.urlopen(request)
            written += 1
        except urllib.error.HTTPError as error:
            failed += 1
            detail = error.read().decode("utf-8", errors="replace")[:200]
            print(f"  FAILED users/{doc_id}: HTTP {error.code} {detail}")
    print(f"done: {written} written, {failed} failed")


if __name__ == "__main__":
    main()
