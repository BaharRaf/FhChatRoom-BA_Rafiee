"""Read-only export of Firestore collections into the pipeline's JSON format.

Closes the operational gap between the live platform and the offline
pipeline: it downloads ``users``, ``rooms``, and every room's ``messages``
subcollection through the Firestore REST API and writes ``users.json``,
``rooms.json``, and ``messages.json`` in exactly the shape
:mod:`recsys.firestore_json_adapter` ingests. Message timestamps are reduced
to a relative ``day`` index at export time; pseudonymisation itself happens
later, at ingestion, inside the adapter (the thesis's trusted boundary).

Safety model (mirrors ``recsys.write_back_payloads``):
  * strictly read-only -- the tool never writes to Firestore;
  * an OAuth2 access token must be supplied explicitly (env var
    ``FIRESTORE_OAUTH_TOKEN`` or ``--token-file``); no credentials are stored.

Usage:
    python3 -m recsys.export_firestore \
        --project <project-id> --output-dir export/

    python3 -m recsys.run_offline_pipeline --mode json \
        --users export/users.json --rooms export/rooms.json \
        --messages export/messages.json --output-dir out/
"""

from __future__ import annotations

import argparse
import json
import os
import sys
import urllib.parse
import urllib.request
from pathlib import Path

BASE = "https://firestore.googleapis.com/v1/projects/{project}/databases/(default)/documents"


def _token(args: argparse.Namespace) -> str:
    if args.token_file:
        return Path(args.token_file).read_text(encoding="utf-8").strip()
    token = os.environ.get("FIRESTORE_OAUTH_TOKEN", "").strip()
    if not token:
        raise SystemExit(
            "No credentials: set FIRESTORE_OAUTH_TOKEN or pass --token-file "
            "(e.g. gcloud auth print-access-token > /tmp/token)."
        )
    return token


def _decode(value: dict) -> object:
    for kind in ("stringValue", "booleanValue", "doubleValue"):
        if kind in value:
            return value[kind]
    if "integerValue" in value:
        return int(value["integerValue"])
    if "timestampValue" in value:
        return value["timestampValue"]
    if "nullValue" in value:
        return None
    if "arrayValue" in value:
        return [_decode(item) for item in value["arrayValue"].get("values", [])]
    if "mapValue" in value:
        return {key: _decode(item) for key, item in value["mapValue"].get("fields", {}).items()}
    return None


def _document_to_dict(document: dict) -> dict:
    row = {key: _decode(value) for key, value in document.get("fields", {}).items()}
    row.setdefault("id", document["name"].rsplit("/", 1)[-1])
    return row


def _list_collection(project: str, token: str, path: str) -> list[dict]:
    documents: list[dict] = []
    page_token = ""
    while True:
        url = f"{BASE.format(project=project)}/{path}?pageSize=300"
        if page_token:
            url += f"&pageToken={urllib.parse.quote(page_token)}"
        request = urllib.request.Request(url, headers={"Authorization": f"Bearer {token}"})
        payload = json.load(urllib.request.urlopen(request, timeout=60))
        documents.extend(_document_to_dict(doc) for doc in payload.get("documents", []))
        page_token = payload.get("nextPageToken", "")
        if not page_token:
            return documents


def _relative_day(timestamp_ms: object, first_timestamp_ms: int) -> int:
    if not isinstance(timestamp_ms, (int, float)) or timestamp_ms <= 0:
        return 1
    return max(1, int((int(timestamp_ms) - first_timestamp_ms) // 86_400_000) + 1)


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--project", required=True, help="Firebase project id")
    parser.add_argument("--output-dir", required=True)
    parser.add_argument("--token-file", help="File containing an OAuth2 access token")
    args = parser.parse_args()

    token = _token(args)
    output_dir = Path(args.output_dir)
    output_dir.mkdir(parents=True, exist_ok=True)

    users = _list_collection(args.project, token, "users")
    rooms = _list_collection(args.project, token, "rooms")

    messages: list[dict] = []
    for room in rooms:
        room_id = str(room.get("id", ""))
        if not room_id:
            continue
        for message in _list_collection(args.project, token, f"rooms/{room_id}/messages"):
            message["roomId"] = room_id
            messages.append(message)

    timestamps = [m["timestamp"] for m in messages if isinstance(m.get("timestamp"), (int, float))]
    first_timestamp = int(min(timestamps)) if timestamps else 0
    for message in messages:
        message["day"] = _relative_day(message.get("timestamp"), first_timestamp)

    (output_dir / "users.json").write_text(json.dumps(users, indent=2), encoding="utf-8")
    (output_dir / "rooms.json").write_text(json.dumps(rooms, indent=2), encoding="utf-8")
    (output_dir / "messages.json").write_text(json.dumps(messages, indent=2), encoding="utf-8")
    print(
        f"Exported {len(users)} users, {len(rooms)} rooms, {len(messages)} messages "
        f"to {output_dir}/ (read-only; pseudonymisation happens at ingestion)."
    )


if __name__ == "__main__":
    main()
