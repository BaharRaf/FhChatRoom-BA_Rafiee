from __future__ import annotations

from pathlib import Path
import unittest

from recsys.baselines import build_firestore_payloads
from recsys.firestore_json_adapter import dataset_from_firestore_json
from recsys.hin import build_hin


FIXTURE_DIR = Path(__file__).parent / "fixtures"


class FirestoreJsonAdapterTest(unittest.TestCase):
    def setUp(self) -> None:
        self.dataset = dataset_from_firestore_json(
            users_path=FIXTURE_DIR / "users.json",
            rooms_path=FIXTURE_DIR / "rooms.json",
            messages_path=FIXTURE_DIR / "messages.json",
        )
        self.hin = build_hin(self.dataset, max_topics=10)

    def test_adapter_loads_snapshot_counts(self) -> None:
        self.assertEqual(len(self.dataset.students), 3)
        self.assertEqual(len(self.dataset.groups), 3)
        self.assertEqual(len(self.dataset.messages), 3)

    def test_student_identifiers_are_pseudonymized(self) -> None:
        self.assertTrue(all("@" not in student_id for student_id in self.dataset.students))
        self.assertTrue(all(student.first_name == "" for student in self.dataset.students.values()))
        self.assertEqual(self.dataset.to_dict()["privacy"]["studentIdentifiers"], "pseudonymized")
        self.assertFalse(self.dataset.to_dict()["privacy"]["sourceIdentifierMappingIncluded"])

    def test_memberships_are_derived_from_rooms(self) -> None:
        anna_id = next(
            student_id
            for student_id, source_id in self.dataset.source_student_ids.items()
            if source_id == "anna@stud.fh-campuswien.ac.at"
        )
        anna = self.dataset.students[anna_id]
        self.assertIn("room-algorithms", anna.joined_group_ids)

    def test_unknown_fixture_paths_keep_topic_defaults_working(self) -> None:
        anna_id = next(
            student_id
            for student_id, source_id in self.dataset.source_student_ids.items()
            if source_id == "anna@stud.fh-campuswien.ac.at"
        )
        anna = self.dataset.students[anna_id]
        self.assertEqual(len(anna.preferred_topics), 3)

    def test_payload_matches_android_contract(self) -> None:
        payloads = build_firestore_payloads(self.dataset, self.hin, top_k=2)
        self.assertIn("anna@stud.fh-campuswien.ac.at", payloads)
        self.assertIn("recommendedRoomIds", payloads["anna@stud.fh-campuswien.ac.at"])
        self.assertEqual(payloads["anna@stud.fh-campuswien.ac.at"]["recommendationSource"], "CONTENT_BASED")

    def test_payload_can_stay_pseudonymized_for_offline_reporting(self) -> None:
        payloads = build_firestore_payloads(
            self.dataset,
            self.hin,
            top_k=2,
            use_source_student_ids=False,
        )
        self.assertTrue(all("@" not in student_id for student_id in payloads))


if __name__ == "__main__":
    unittest.main()
