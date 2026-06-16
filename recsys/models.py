from __future__ import annotations

from collections import Counter
from dataclasses import asdict, dataclass, field
from datetime import datetime, timezone
from typing import Any


def semester_bucket_for(semester: int) -> str:
    if semester <= 0:
        return "Unknown"
    if semester <= 2:
        return "1-2"
    if semester <= 4:
        return "3-4"
    if semester <= 6:
        return "5-6"
    return "7+"


def utc_timestamp() -> str:
    return datetime.now(timezone.utc).isoformat()


@dataclass
class Student:
    id: str
    first_name: str
    last_name: str
    study_path: str
    semester: int
    semester_bucket: str
    preferred_topics: list[str]
    joined_group_ids: list[str] = field(default_factory=list)


@dataclass
class StudyGroup:
    id: str
    name: str
    category: str
    description: str
    primary_study_path: str
    topic_tags: list[str]
    member_ids: list[str] = field(default_factory=list)
    # Origin of the group. Student-made (organic, peer-led) groups are the
    # recommendation target; academic/template rooms (auto-assigned by study
    # path and semester) are scaffolding that stays in the graph as context
    # but is never itself recommended. Defaults to True so plain synthetic
    # groups and minimal fixtures are treated as recommendable.
    is_student_made: bool = True


@dataclass
class Message:
    id: str
    sender_id: str
    group_id: str
    text: str
    day: int
    reaction_count: int


@dataclass(frozen=True)
class DatasetConfig:
    num_students: int = 120
    num_groups: int = 24
    num_topics: int = 30
    messages_per_day: int = 250
    num_days: int = 14
    min_groups_per_student: int = 2
    max_groups_per_student: int = 4


@dataclass
class SyntheticDataset:
    config: DatasetConfig
    students: dict[str, Student]
    groups: dict[str, StudyGroup]
    messages: list[Message]
    generated_at: str
    seed: int
    source_student_ids: dict[str, str] = field(default_factory=dict, repr=False)

    def source_student_id_for(self, student_id: str) -> str:
        return self.source_student_ids.get(student_id, student_id)

    def _message_dict(self, message: "Message", redact_text: bool) -> dict[str, Any]:
        if not redact_text:
            return asdict(message)
        # Privacy boundary: persisted artifacts must not contain raw message
        # bodies. Bodies are reduced to the derived statistics the HIN needs
        # (length, reaction count, day); topics are extracted into the
        # aggregate HIN, never stored per message.
        return {
            "id": message.id,
            "sender_id": message.sender_id,
            "group_id": message.group_id,
            "length": len(message.text.split()),
            "day": message.day,
            "reaction_count": message.reaction_count,
        }

    def to_dict(self, redact_message_text: bool = True) -> dict[str, Any]:
        return {
            "config": asdict(self.config),
            "generatedAt": self.generated_at,
            "seed": self.seed,
            "privacy": {
                "studentIdentifiers": "pseudonymized" if self.source_student_ids else "internal",
                "sourceIdentifierMappingIncluded": False,
                "messageBodies": "redacted" if redact_message_text else "raw",
            },
            "students": {student_id: asdict(student) for student_id, student in self.students.items()},
            "groups": {group_id: asdict(group) for group_id, group in self.groups.items()},
            "messages": [self._message_dict(message, redact_message_text) for message in self.messages],
        }


@dataclass(frozen=True)
class HINNode:
    id: str
    node_type: str
    features: dict[str, Any]


@dataclass(frozen=True)
class HINEdge:
    source: str
    target: str
    relation: str
    weight: float = 1.0


@dataclass
class HINGraph:
    nodes: dict[str, HINNode]
    edges: list[HINEdge]
    group_topic_weights: dict[str, dict[str, float]]
    student_topic_weights: dict[str, dict[str, float]]
    selected_topics: list[str]

    def summary(self) -> dict[str, Any]:
        node_counts = Counter(node.node_type for node in self.nodes.values())
        edge_counts = Counter(edge.relation for edge in self.edges)
        return {
            "nodeCounts": dict(node_counts),
            "edgeCounts": dict(edge_counts),
            "numTopics": len(self.selected_topics),
            "selectedTopics": self.selected_topics,
        }


@dataclass(frozen=True)
class RecommendationBreakdown:
    topic_similarity: float
    study_path_affinity: float
    semester_proximity: float
    popularity: float
    serendipity: float
    relevance: float


@dataclass(frozen=True)
class Recommendation:
    student_id: str
    group_id: str
    group_name: str
    score: float
    breakdown: RecommendationBreakdown

    def to_dict(self) -> dict[str, Any]:
        return {
            "studentId": self.student_id,
            "groupId": self.group_id,
            "groupName": self.group_name,
            "score": round(self.score, 6),
            "breakdown": {
                "topicSimilarity": round(self.breakdown.topic_similarity, 6),
                "studyPathAffinity": round(self.breakdown.study_path_affinity, 6),
                "semesterProximity": round(self.breakdown.semester_proximity, 6),
                "popularity": round(self.breakdown.popularity, 6),
                "serendipity": round(self.breakdown.serendipity, 6),
                "relevance": round(self.breakdown.relevance, 6),
            },
        }
