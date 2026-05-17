# AI Pipeline Package

This package separates the offline recommendation, training, and evaluation code from the
Android application code.

It is the official offline recommender package for BA2 thesis work going forward.

The Android app remains under `app/`, while the offline recommender remains under
`ai_pipeline/`. This keeps the training code out of the Android runtime and matches the
thesis architecture: app + Firebase + separate local Python pipeline.

## Structure

- `ai_pipeline.data`
  - dataset models
  - synthetic data generation
  - Firestore JSON snapshot adapter
- `ai_pipeline.graph`
  - HIN construction and tokenization
- `ai_pipeline.training`
  - baseline recommender
  - GraphSAGE prep and training
  - LightGCN training
  - offline evaluation helpers
- `ai_pipeline.cli`
  - runnable entrypoints

## Recommended commands

```bash
python3 -m ai_pipeline.cli.run_demo
python3 -m ai_pipeline.cli.run_from_json --users path/to/users.json --rooms path/to/rooms.json --messages path/to/messages.json --output-dir /tmp/recsys-from-json
python3 -m ai_pipeline.cli.run_graphsage_prep --mode json --users path/to/users.json --rooms path/to/rooms.json --messages path/to/messages.json --output-dir /tmp/recsys-graphsage-prep
python3 -m ai_pipeline.cli.run_graphsage_train --mode json --users path/to/users.json --rooms path/to/rooms.json --messages path/to/messages.json --output-dir /tmp/recsys-graphsage-train
python3 -m ai_pipeline.cli.run_lightgcn_train --mode json --users path/to/users.json --rooms path/to/rooms.json --messages path/to/messages.json --output-dir /tmp/recsys-lightgcn-train
python3 -m ai_pipeline.cli.run_evaluation --mode json --users path/to/users.json --rooms path/to/rooms.json --messages path/to/messages.json --output-dir /tmp/recsys-eval
```

## Compatibility

The original `recsys` package is still kept in the repository as a compatibility layer for the
existing tests, scripts, and older documentation.
