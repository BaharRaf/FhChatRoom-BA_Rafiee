from __future__ import annotations

import hashlib
import json
from pathlib import Path
from typing import Any

from recsys.hin import tokenize
from recsys.models import DatasetConfig
from recsys.models import Message
from recsys.models import Student
from recsys.models import StudyGroup
from recsys.models import SyntheticDataset
from recsys.models import semester_bucket_for
from recsys.models import utc_timestamp
from recsys.synthetic_data import STUDY_PATH_TOPICS


FIRESTORE_STUDY_PATH_TOPICS = {
    **STUDY_PATH_TOPICS,
    "Angewandte Elektronik und Technische Informatik": STUDY_PATH_TOPICS["Applied Electronics"],
    "Bioengineering": [
        "bioprocess", "biochemistry", "microbiology", "cell", "lab", "measurement", "engineering"
    ],
    "Gesundheits- und Krankenpflege": STUDY_PATH_TOPICS["Health Care and Nursing"],
    "Green Mobility": [
        "mobility", "electromobility", "battery", "charging", "automotive", "infrastructure", "sustainability"
    ],
    "Physiotherapie": [
        "anatomy", "rehabilitation", "movement", "therapy", "clinical", "exercise", "assessment"
    ],
    "Public Management": [
        "policy", "governance", "administration", "law", "budget", "organization", "management"
    ],
    "Soziale Arbeit": STUDY_PATH_TOPICS["Social Work"],
}

DEFAULT_PSEUDONYM_SALT = "fhchatroom-ba2-local"


class StudentIdMapper:
    def __init__(self, pseudonymize: bool, salt: str = DEFAULT_PSEUDONYM_SALT) -> None:
        self.pseudonymize = pseudonymize
        self.salt = salt
        self.source_by_internal: dict[str, str] = {}

    def internal_id_for(self, source_id: str) -> str:
        cleaned_source_id = source_id.strip()
        if not cleaned_source_id:
            return ""
        if not self.pseudonymize:
            return cleaned_source_id

        digest = hashlib.sha256(
            f"{self.salt}:{cleaned_source_id.lower()}".encode("utf-8")
        ).hexdigest()
        internal_id = f"student-{digest[:16]}"
        self.source_by_internal.setdefault(internal_id, cleaned_source_id)
        return internal_id


def _load_json(path: Path) -> Any:
    return json.loads(path.read_text(encoding="utf-8"))


def _normalize_documents(payload: Any) -> list[dict[str, Any]]:
    if isinstance(payload, list):
        return [item for item in payload if isinstance(item, dict)]

    if isinstance(payload, dict):
        documents: list[dict[str, Any]] = []
        for key, value in payload.items():
            if not isinstance(value, dict):
                continue
            document = dict(value)
            document.setdefault("id", key)
            documents.append(document)
        return documents

    raise ValueError("Expected a JSON list or object of documents.")


def _coerce_bool(*values: Any) -> bool:
    """True if any of the given Firestore field values is truthy.

    Firestore exports may carry booleans as real booleans or as strings
    ("true"/"True"); absent fields arrive as None.
    """
    for value in values:
        if isinstance(value, bool) and value:
            return True
        if isinstance(value, str) and value.strip().lower() == "true":
            return True
    return False


def _coerce_int(value: Any, default: int = 0) -> int:
    if value is None:
        return default
    if isinstance(value, bool):
        return int(value)
    if isinstance(value, (int, float)):
        return int(value)
    if isinstance(value, str):
        digits = "".join(character for character in value if character.isdigit())
        return int(digits) if digits else default
    return default


def _reaction_count(value: Any) -> int:
    if isinstance(value, dict):
        return len(value)
    if isinstance(value, list):
        return len(value)
    return _coerce_int(value, default=0)


def _default_topics_for_study_path(study_path: str) -> list[str]:
    topics = FIRESTORE_STUDY_PATH_TOPICS.get(study_path, [])
    return topics[:3] if topics else ["general", "collaboration", "study"]


def _infer_room_topic_tags(category: str, description: str, message_texts: list[str]) -> list[str]:
    tokens = tokenize(" ".join([category, description, *message_texts]))
    unique_tokens: list[str] = []
    for token in tokens:
        if token not in unique_tokens:
            unique_tokens.append(token)
        if len(unique_tokens) == 3:
            break
    return unique_tokens or ["general"]


def dataset_from_firestore_json(
    users_path: str | Path,
    rooms_path: str | Path,
    messages_path: str | Path,
    seed: int = 0,
    pseudonymize_students: bool = True,
    pseudonym_salt: str = DEFAULT_PSEUDONYM_SALT,
) -> SyntheticDataset:
    users_payload = _normalize_documents(_load_json(Path(users_path)))
    rooms_payload = _normalize_documents(_load_json(Path(rooms_path)))
    messages_payload = _normalize_documents(_load_json(Path(messages_path)))

    student_id_mapper = StudentIdMapper(
        pseudonymize=pseudonymize_students,
        salt=pseudonym_salt,
    )
    students: dict[str, Student] = {}
    groups: dict[str, StudyGroup] = {}
    messages: list[Message] = []
    room_message_texts: dict[str, list[str]] = {}

    for user in users_payload:
        source_student_id = str(user.get("email") or user.get("id") or "").strip()
        student_id = student_id_mapper.internal_id_for(source_student_id)
        if not student_id:
            continue

        study_path = str(user.get("studyPath") or user.get("study_path") or "").strip()
        semester = _coerce_int(user.get("semester"), default=0)
        students[student_id] = Student(
            id=student_id,
            first_name="" if pseudonymize_students else str(user.get("firstName") or user.get("first_name") or "").strip(),
            last_name="" if pseudonymize_students else str(user.get("lastName") or user.get("last_name") or "").strip(),
            study_path=study_path,
            semester=semester,
            semester_bucket=semester_bucket_for(semester),
            preferred_topics=list(user.get("preferredTopics") or _default_topics_for_study_path(study_path)),
            joined_group_ids=[],
        )

    # Privacy boundary: private rooms and direct messages must never enter
    # the recommendation graph -- neither as recommendable items nor as
    # membership or message evidence.
    non_public_room_ids = {
        str(room.get("id") or "").strip()
        for room in rooms_payload
        if _coerce_bool(room.get("isPrivate"), room.get("private"))
        or _coerce_bool(room.get("isDirect"), room.get("direct"))
    }

    for message_doc in messages_payload:
        room_id = str(message_doc.get("roomId") or message_doc.get("groupId") or "").strip()
        if room_id in non_public_room_ids:
            continue
        source_sender_id = str(message_doc.get("senderId") or message_doc.get("senderEmail") or "").strip()
        sender_id = student_id_mapper.internal_id_for(source_sender_id)
        message_id = str(message_doc.get("id") or "").strip()
        # Data minimisation: the original message string never leaves the
        # ingestion step. Only its lower-cased content tokens -- the exact
        # input the TF-IDF topic extraction needs -- are retained and
        # persisted in downstream artifacts.
        text = " ".join(tokenize(str(message_doc.get("text") or "")))

        if not room_id or not sender_id or not message_id:
            continue

        room_message_texts.setdefault(room_id, []).append(text)
        messages.append(
            Message(
                id=message_id,
                sender_id=sender_id,
                group_id=room_id,
                text=text,
                day=max(1, _coerce_int(message_doc.get("day"), default=1)),
                reaction_count=_reaction_count(message_doc.get("reactions") or message_doc.get("reactionCount")),
            )
        )

        if sender_id not in students:
            students[sender_id] = Student(
                id=sender_id,
                first_name="",
                last_name="",
                study_path="",
                semester=0,
                semester_bucket=semester_bucket_for(0),
                preferred_topics=["general", "collaboration", "study"],
                joined_group_ids=[],
            )

    for room in rooms_payload:
        group_id = str(room.get("id") or "").strip()
        if not group_id or group_id in non_public_room_ids:
            continue

        member_ids = [
            student_id_mapper.internal_id_for(str(member))
            for member in room.get("members", [])
            if str(member).strip()
        ]
        category = str(room.get("category") or "").strip()
        description = str(room.get("description") or "").strip()
        topic_tags = list(room.get("topicTags") or [])
        if not topic_tags:
            topic_tags = _infer_room_topic_tags(category, description, room_message_texts.get(group_id, []))

        # Academic/template rooms (auto-assigned by study path and semester)
        # are scaffolding, not recommendation targets. Detect them from any of
        # the markers the app writes; everything else is a student-made group.
        is_academic = (
            _coerce_bool(room.get("templateRoom"))
            or str(room.get("ownerEmail") or "").strip().lower() == "system"
            or bool(str(room.get("academicRoomKind") or "").strip())
        )

        groups[group_id] = StudyGroup(
            id=group_id,
            name=str(room.get("name") or group_id).strip(),
            category=category or topic_tags[0].title(),
            description=description,
            primary_study_path=str(room.get("primaryStudyPath") or "").strip(),
            topic_tags=topic_tags[:3],
            member_ids=member_ids,
            is_student_made=not is_academic,
        )

        for member_id in member_ids:
            if member_id not in students:
                students[member_id] = Student(
                    id=member_id,
                    first_name="",
                    last_name="",
                    study_path="",
                    semester=0,
                    semester_bucket=semester_bucket_for(0),
                    preferred_topics=["general", "collaboration", "study"],
                    joined_group_ids=[],
                )
            if group_id not in students[member_id].joined_group_ids:
                students[member_id].joined_group_ids.append(group_id)

    for student in students.values():
        if not student.preferred_topics:
            student.preferred_topics = _default_topics_for_study_path(student.study_path)

    max_day = max((message.day for message in messages), default=1)
    unique_topics = {
        topic
        for group in groups.values()
        for topic in group.topic_tags
    }

    return SyntheticDataset(
        config=DatasetConfig(
            num_students=len(students),
            num_groups=len(groups),
            num_topics=max(len(unique_topics), 1),
            messages_per_day=max(1, len(messages) // max(max_day, 1)),
            num_days=max_day,
            min_groups_per_student=0,
            max_groups_per_student=max((len(student.joined_group_ids) for student in students.values()), default=0),
        ),
        students=students,
        groups=groups,
        messages=messages,
        generated_at=utc_timestamp(),
        seed=seed,
        source_student_ids=student_id_mapper.source_by_internal if pseudonymize_students else {},
    )
