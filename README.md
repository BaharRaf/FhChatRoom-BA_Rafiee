# FhChatRoom — BA Project (Bahareh Rafiee)

FhChatRoom is a mobile study-group collaboration platform for FH Campus Wien
(Android, Kotlin/Jetpack Compose, Firebase) extended with a **graph-based,
privacy-aware study-group recommender** — the subject of Bachelor Theses 1
and 2.

- **BA1** (theory): HIN model of the platform, GraphSAGE vs. LightGCN
  argument for the semester cold-start problem, Privacy-by-Design
  architecture, frozen evaluation protocol.
- **BA2** (this repository's `recsys/` + `docs/ba2/`): full implementation
  and empirical validation.

## Repository layout

```
app/                     Android client (Kotlin, Jetpack Compose, MVVM)
recsys/                  Offline recommendation pipeline (pure NumPy/SciPy)
recsys/tests/            67 unit & integration tests (pytest)
recsys_experiments/      Experiment output: results.json, summary.md, figures/
docs/ba2/                BA2 thesis (LaTeX), defence slides, study guide
```

## Quick start (experiments)

Requirements: Python 3.11+, `numpy`, `scipy`, `matplotlib`, `pytest`.

```bash
# run the test suite
python3 -m pytest recsys/tests -q

# run the complete BA1-Chapter-7 evaluation protocol
# (6 models, statistics, fairness audit, 21-run DP sweep; ~6-8 min on CPU)
python3 -m recsys.run_experiments --output-dir recsys_experiments

# production-shaped end-to-end pipeline (synthetic or Firestore JSON export)
python3 -m recsys.run_offline_pipeline --help
```

Every run is deterministic (seed 42 by default); `results.json`,
`summary.md`, and all thesis figures are regenerated in
`recsys_experiments/`.

## Thesis build

```bash
python3 docs/ba2/gen_results_macros.py        # inject experiment numbers
cd docs/ba2
pdflatex thesis && bibtex thesis && pdflatex thesis && pdflatex thesis
cd defence && pdflatex presentation && pdflatex presentation
```

All quantitative claims in the thesis are LaTeX macros generated from
`results.json` — the text can never drift from the measurements.

## Android app

Open the project in Android Studio and run the `app` configuration, or:

```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

The offline pipeline writes exactly three fields to `users/{email}` in
Firestore — `recommendedRoomIds`, `recommendationsUpdatedAt`, and
`recommendationSource` (see `recsys/write_back_payloads.py`,
`ALLOWED_FIELDS`). Per-recommendation score breakdowns are produced for
inspection in the local payload files
(`graphsage_firestore_payloads.json`) but are deliberately not written to
the user document, keeping it minimal and matching the Android `User`
model. `RecommendationViewModel` observes that document and the room list
screen renders the "Recommended for you" section with one-tap join.

## Firebase security rules

Access control is versioned as code: `firestore.rules`, `storage.rules`, and
`database.rules.json` (config in `firebase.json`). Their behaviour is proven
by a security-rules test suite that runs against the local emulator — no
credentials, no live project:

```bash
cd firebase-tests && npm install && npm test    # 21 passing
```

The suite asserts, among others: only `@stud.hcw.ac.at` accounts can create
user docs; the recommendation fields are not client-writable; room members
may react/hide but not edit others' messages; non-owners cannot change room
visibility; a client cannot mint arbitrary `system`-owned rooms; and chat
media uploads are type/size guarded. Deploy with
`firebase deploy --only firestore:rules,storage,database` (or paste via the
Console) and keep repo and console in sync.

## Key documents

- `docs/ba2/thesis.tex` — Bachelor Thesis 2 (≈30 pages)
- `docs/ba2/defence/presentation.tex` — defence slide deck
- `docs/ba2/STUDY_GUIDE.md` — concepts, code tour, Q&A for the defence
- `recsys_experiments/summary.md` — latest experiment summary
