from __future__ import annotations

from contextlib import redirect_stdout
import io
import json
from argparse import Namespace
from pathlib import Path
from tempfile import TemporaryDirectory
import unittest
from unittest.mock import patch

from recsys.run_offline_pipeline import main


FIXTURE_DIR = Path(__file__).parent / "fixtures"


class OfflinePipelineCliTest(unittest.TestCase):
    def test_json_pipeline_writes_expected_artifacts(self) -> None:
        with TemporaryDirectory() as output_dir:
            argv = [
                "run_offline_pipeline",
                "--mode",
                "json",
                "--users",
                str(FIXTURE_DIR / "users.json"),
                "--rooms",
                str(FIXTURE_DIR / "rooms.json"),
                "--messages",
                str(FIXTURE_DIR / "messages.json"),
                "--epochs",
                "3",
                "--top-k",
                "2",
                "--output-dir",
                output_dir,
            ]

            with patch("sys.argv", argv), redirect_stdout(io.StringIO()):
                main()

            expected_files = {
                "snapshot_dataset.json",
                "hin_summary.json",
                "baseline_firestore_payloads.json",
                "graphsage_firestore_payloads.json",
                "lightgcn_firestore_payloads.json",
                "evaluation_report.json",
                "pipeline_manifest.json",
            }
            written_files = {path.name for path in Path(output_dir).iterdir()}
            self.assertTrue(expected_files.issubset(written_files))

            dataset = json.loads((Path(output_dir) / "snapshot_dataset.json").read_text(encoding="utf-8"))
            self.assertEqual(dataset["privacy"]["studentIdentifiers"], "pseudonymized")
            self.assertTrue(all("@" not in student_id for student_id in dataset["students"]))

            payloads = json.loads(
                (Path(output_dir) / "graphsage_firestore_payloads.json").read_text(encoding="utf-8")
            )
            self.assertIn("anna@stud.fh-campuswien.ac.at", payloads)

    def test_synthetic_pipeline_writes_evaluation_report(self) -> None:
        with TemporaryDirectory() as output_dir:
            args = Namespace(
                mode="synthetic",
                students=12,
                groups=6,
                topics=8,
                messages_per_day=10,
                days=3,
                seed=9,
                epochs=3,
                learning_rate=0.05,
                graphsage_epochs=None,
                graphsage_learning_rate=None,
                lightgcn_epochs=None,
                lightgcn_learning_rate=None,
                negative_samples_per_positive=2,
                hard_negative_ratio=0.67,
                cold_start_interaction_threshold=5,
                min_warm_students=0,
                min_cold_students=0,
                min_held_out_users=1,
                skip_quality_check=False,
                hidden_dim=16,
                embedding_dim=8,
                lightgcn_layers=2,
                top_k=3,
                evaluation_protocol="temporal-onboarding",
                cold_start_ratio=0.25,
                min_temporal_cold_users=1,
                users=None,
                rooms=None,
                messages=None,
                pseudonym_salt="test",
                no_pseudonymize_students=False,
                output_dir=output_dir,
            )

            with patch("recsys.run_offline_pipeline.parse_args", return_value=args), redirect_stdout(io.StringIO()):
                main()

            report = json.loads((Path(output_dir) / "evaluation_report.json").read_text(encoding="utf-8"))
            self.assertEqual(report["evaluationProtocol"], "temporal-onboarding")
            self.assertIn("baseline", report)
            self.assertIn("graphsageLocal", report)
            self.assertIn("lightgcnLocal", report)

    def test_quality_check_rejects_missing_cold_start_segment(self) -> None:
        with TemporaryDirectory() as output_dir:
            args = Namespace(
                mode="synthetic",
                students=20,
                groups=8,
                topics=10,
                messages_per_day=200,
                days=5,
                seed=4,
                epochs=3,
                learning_rate=0.05,
                graphsage_epochs=None,
                graphsage_learning_rate=None,
                lightgcn_epochs=None,
                lightgcn_learning_rate=None,
                negative_samples_per_positive=2,
                hard_negative_ratio=0.67,
                cold_start_interaction_threshold=5,
                min_warm_students=1,
                min_cold_students=1,
                min_held_out_users=1,
                skip_quality_check=False,
                hidden_dim=16,
                embedding_dim=8,
                lightgcn_layers=2,
                top_k=3,
                evaluation_protocol="leave-one-out",
                cold_start_ratio=0.25,
                min_temporal_cold_users=1,
                users=None,
                rooms=None,
                messages=None,
                pseudonym_salt="test",
                no_pseudonymize_students=False,
                output_dir=output_dir,
            )

            with patch("recsys.run_offline_pipeline.parse_args", return_value=args), redirect_stdout(io.StringIO()):
                with self.assertRaises(SystemExit) as error:
                    main()

            self.assertIn("cold-start students", str(error.exception))


if __name__ == "__main__":
    unittest.main()
