#!/usr/bin/env python3
"""Offline regenerator for the Android academic-lecture catalog.

The app reads a *static* catalog (AcademicLectureCatalog.kt) and never scrapes
the FH website at runtime -- that keeps the app offline, deterministic, and
demo-safe. This tool is the OFFLINE, build-time way to refresh that static
data, so updating the catalog later is one command instead of a hand-edit of a
~3,000-line Kotlin file.

Data is decoupled into a reviewable source of truth, tools/academic_catalog.tsv
(tab-separated: StudyPath <TAB> Semester <TAB> "Lecture Title | Type").

Subcommands
-----------
  extract   AcademicLectureCatalog.kt  ->  academic_catalog.tsv
            (one-time bootstrap: pull the current embedded data into the TSV)

  generate  academic_catalog.tsv  ->  AcademicLectureCatalog.kt
            (regenerate the Kotlin file from the reviewed TSV)

  fetch     public FH study pages  ->  *.candidate.tsv  (best effort)
            Pulls a program's public curriculum page and extracts candidate
            lecture rows for HUMAN REVIEW. It deliberately writes a separate
            *.candidate.tsv and never overwrites the reviewed catalog, because
            the public pages' HTML varies per program and must be checked
            before becoming app data. Requires network; optional.

Typical refresh workflow
------------------------
  python3 tools/refresh_catalog.py extract            # once, to seed the TSV
  # ...edit tools/academic_catalog.tsv (or merge reviewed fetch candidates)...
  python3 tools/refresh_catalog.py generate           # rewrite the Kotlin
  ./gradlew :app:compileDebugKotlin                   # verify it builds

Runtime is unaffected: the generated file still parses the same static string.
A live CAMPUSonline REST sync is noted as future work in the thesis; it is
intentionally NOT a runtime dependency (auth-gated API, demo fragility).
"""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parent.parent
KOTLIN_FILE = REPO_ROOT / "app/src/main/java/com/example/fhchatroom/data/AcademicLectureCatalog.kt"
TSV_FILE = REPO_ROOT / "tools/academic_catalog.tsv"

ROWS_PER_BLOCK = 500  # keep each Kotlin string literal well under the 64 KB limit

# The parser is reproduced verbatim so `generate` emits a complete, compilable
# file. It must stay byte-identical to the runtime parser in the Kotlin source.
PARSER_BLOCK = '''
private fun parseAcademicLectureCatalog(rows: String): Map<String, Map<Long, List<String>>> {
    val catalog = linkedMapOf<String, MutableMap<Long, MutableList<String>>>()

    rows.lineSequence()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .forEach { row ->
            val parts = row.split('\\t', limit = 3)
            if (parts.size != 3) {
                return@forEach
            }

            val studyPath = parts[0]
            val semester = parts[1].toLongOrNull() ?: return@forEach
            val lectureName = parts[2]
            val studyCatalog = catalog.getOrPut(studyPathCatalogKey(studyPath)) { linkedMapOf() }
            val lectures = studyCatalog.getOrPut(semester) { mutableListOf() }

            if (lectureName !in lectures) {
                lectures.add(lectureName)
            }
        }

    return catalog.mapValues { (_, semesters) ->
        semesters.mapValues { (_, lectures) -> lectures.toList() }
    }
}
'''


def _parse_rows_from_kotlin(text: str) -> list[tuple[str, int, str]]:
    """Extracts (studyPath, semester, lecture) rows from the embedded blocks."""
    rows: list[tuple[str, int, str]] = []
    # Each data block is a triple-quoted string passed to append(...).
    for block in re.findall(r'"""(.*?)"""', text, flags=re.DOTALL):
        for line in block.splitlines():
            line = line.strip()
            if not line:
                continue
            parts = line.split("\t")
            if len(parts) != 3:
                continue
            study_path, semester, lecture = parts
            if not semester.isdigit():
                continue
            rows.append((study_path, int(semester), lecture))
    return rows


def _read_tsv() -> list[tuple[str, int, str]]:
    rows: list[tuple[str, int, str]] = []
    for line in TSV_FILE.read_text(encoding="utf-8").splitlines():
        line = line.rstrip("\n")
        if not line.strip():
            continue
        parts = line.split("\t")
        if len(parts) != 3 or not parts[1].isdigit():
            continue
        rows.append((parts[0], int(parts[1]), parts[2]))
    return rows


def _dedupe(rows: list[tuple[str, int, str]]) -> list[tuple[str, int, str]]:
    """Stable de-dup, preserving first-seen order (matches the parser's intent)."""
    seen: set[tuple[str, int, str]] = set()
    out: list[tuple[str, int, str]] = []
    for row in rows:
        if row not in seen:
            seen.add(row)
            out.append(row)
    return out


def cmd_extract(_: argparse.Namespace) -> int:
    rows = _dedupe(_parse_rows_from_kotlin(KOTLIN_FILE.read_text(encoding="utf-8")))
    TSV_FILE.parent.mkdir(parents=True, exist_ok=True)
    TSV_FILE.write_text(
        "".join(f"{sp}\t{sem}\t{lec}\n" for sp, sem, lec in rows), encoding="utf-8"
    )
    print(f"extracted {len(rows)} rows -> {TSV_FILE.relative_to(REPO_ROOT)}")
    return 0


def _render_kotlin(rows: list[tuple[str, int, str]]) -> str:
    lines = [f"{sp}\t{sem}\t{lec}" for sp, sem, lec in rows]
    blocks = [lines[i : i + ROWS_PER_BLOCK] for i in range(0, len(lines), ROWS_PER_BLOCK)]

    out: list[str] = [
        "package com.example.fhchatroom.data",
        "",
        "// Generated by tools/refresh_catalog.py from tools/academic_catalog.tsv.",
        "// Runtime stays offline: the app reads this static catalog and never scrapes the website.",
        "// Do not edit by hand -- edit the TSV and re-run `generate`.",
        "internal val lectureCatalogByStudyPath: Map<String, Map<Long, List<String>>> by lazy {",
        "    parseAcademicLectureCatalog(academicLectureCatalogRows())",
        "}",
        "",
        "private fun academicLectureCatalogRows(): String = buildString {",
    ]
    for block in blocks:
        body = "\n".join(block)
        out.append("    append(")
        out.append('        """')
        out.append(body)
        out.append('""".trimIndent()')
        out.append("    )")
        out.append("    append('\\n')")
    out.append("}")
    out.append(PARSER_BLOCK.rstrip("\n"))
    out.append("")
    return "\n".join(out)


def cmd_generate(_: argparse.Namespace) -> int:
    rows = _dedupe(_read_tsv())
    if not rows:
        print("refusing to generate: TSV is empty (run `extract` first)", file=sys.stderr)
        return 1
    KOTLIN_FILE.write_text(_render_kotlin(rows), encoding="utf-8")
    print(f"generated {KOTLIN_FILE.relative_to(REPO_ROOT)} from {len(rows)} rows")
    return 0


def cmd_verify(_: argparse.Namespace) -> int:
    """Round-trip check: Kotlin -> rows -> Kotlin -> rows must be identical."""
    original = _dedupe(_parse_rows_from_kotlin(KOTLIN_FILE.read_text(encoding="utf-8")))
    regenerated = _dedupe(_parse_rows_from_kotlin(_render_kotlin(original)))
    if original == regenerated:
        print(f"round-trip OK: {len(original)} rows preserved exactly")
        return 0
    print("round-trip MISMATCH", file=sys.stderr)
    return 1


def cmd_fetch(args: argparse.Namespace) -> int:
    """Best-effort fetch of a public program page into a candidate TSV for review."""
    try:
        import urllib.request
    except Exception as exc:  # pragma: no cover
        print(f"network unavailable: {exc}", file=sys.stderr)
        return 1
    req = urllib.request.Request(args.url, headers={"User-Agent": "fhchatroom-catalog-refresh"})
    html = urllib.request.urlopen(req, timeout=30).read().decode("utf-8", "ignore")
    # Best-effort: lecture titles on the curriculum pages typically appear as
    # list/table cells. This heuristic extracts visible text in <li>/<td> and is
    # intentionally conservative -- a human MUST review the candidate file and
    # assign study path + semester before merging into academic_catalog.tsv.
    candidates = re.findall(r"<(?:li|td)[^>]*>(.*?)</(?:li|td)>", html, flags=re.DOTALL | re.IGNORECASE)
    cleaned = []
    for c in candidates:
        text = re.sub(r"<[^>]+>", "", c).strip()
        if 3 <= len(text) <= 120 and not text.startswith("http"):
            cleaned.append(text)
    out_path = REPO_ROOT / "tools" / f"{args.study_path}.candidate.tsv"
    out_path.write_text(
        "".join(f"{args.study_path}\t{args.semester}\t{t}\n" for t in dict.fromkeys(cleaned)),
        encoding="utf-8",
    )
    print(
        f"wrote {len(set(cleaned))} CANDIDATE rows -> {out_path.relative_to(REPO_ROOT)}\n"
        "Review/clean it, then merge the good rows into tools/academic_catalog.tsv "
        "and run `generate`. (Page HTML varies per program; do not trust blindly.)"
    )
    return 0


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    sub = parser.add_subparsers(dest="command", required=True)
    sub.add_parser("extract", help="Kotlin catalog -> TSV (bootstrap the source of truth)")
    sub.add_parser("generate", help="TSV -> regenerate the Kotlin catalog")
    sub.add_parser("verify", help="round-trip fidelity check (no files written)")
    p_fetch = sub.add_parser("fetch", help="best-effort fetch a program page into a candidate TSV")
    p_fetch.add_argument("--url", required=True, help="public curriculum page URL")
    p_fetch.add_argument("--study-path", required=True, help="study path name (catalog key)")
    p_fetch.add_argument("--semester", type=int, required=True, help="semester number")

    args = parser.parse_args()
    return {
        "extract": cmd_extract,
        "generate": cmd_generate,
        "verify": cmd_verify,
        "fetch": cmd_fetch,
    }[args.command](args)


if __name__ == "__main__":
    raise SystemExit(main())
