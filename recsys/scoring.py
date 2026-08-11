"""Shared scoring signals used by the recommenders.

These helpers were historically duplicated across ``baselines``,
``graphsage_train``, and ``lightgcn_train``; this module is their single
home. Behaviour is preserved exactly as it was when the thesis numbers were
produced -- including one deliberate divergence between rankers, which is
kept explicit here instead of hidden in copies:

* ``study_path_affinity`` for an *empty* candidate group: the GraphSAGE
  ranker falls back to matching the group's primary study path
  (``empty_group_uses_primary_path=True``), while the Content-Based ranker
  scores an empty group 0.0. Both behaviours are frozen because every
  quantitative result in the thesis was generated with them.
"""

from __future__ import annotations

from statistics import median

import numpy as np

from recsys.models import SyntheticDataset


def cosine_similarity(left: np.ndarray, right: np.ndarray) -> float:
    denominator = np.linalg.norm(left) * np.linalg.norm(right)
    if denominator == 0.0:
        return 0.0
    return float(np.dot(left, right) / denominator)


def sparse_cosine_similarity(left: dict[str, float], right: dict[str, float]) -> float:
    if not left or not right:
        return 0.0
    shared = set(left) & set(right)
    numerator = sum(left[key] * right[key] for key in shared)
    left_norm = sum(value * value for value in left.values()) ** 0.5
    right_norm = sum(value * value for value in right.values()) ** 0.5
    if left_norm == 0.0 or right_norm == 0.0:
        return 0.0
    return numerator / (left_norm * right_norm)


def jaccard_similarity(left: set[str], right: set[str]) -> float:
    union = left | right
    if not union:
        return 0.0
    return len(left & right) / len(union)


def peer_set(dataset: SyntheticDataset, student_id: str) -> set[str]:
    peer_ids: set[str] = set()
    student = dataset.students[student_id]
    for group_id in student.joined_group_ids:
        peer_ids.update(dataset.groups[group_id].member_ids)
    peer_ids.discard(student_id)
    return peer_ids


def study_path_affinity(
    dataset: SyntheticDataset,
    student_id: str,
    group_id: str,
    empty_group_uses_primary_path: bool,
) -> float:
    student = dataset.students[student_id]
    group = dataset.groups[group_id]
    if not group.member_ids:
        if empty_group_uses_primary_path:
            return 1.0 if group.primary_study_path == student.study_path else 0.0
        return 0.0
    matching_members = sum(
        1
        for member_id in group.member_ids
        if dataset.students[member_id].study_path == student.study_path
    )
    return matching_members / len(group.member_ids)


def semester_proximity(dataset: SyntheticDataset, student_id: str, group_id: str) -> float:
    student = dataset.students[student_id]
    group = dataset.groups[group_id]
    if not group.member_ids:
        return 0.0
    group_median = median(dataset.students[member_id].semester for member_id in group.member_ids)
    return 1.0 / (1.0 + abs(student.semester - group_median))


def max_group_size(dataset: SyntheticDataset) -> int:
    """Largest member count over all groups; hoist this out of ranking loops.

    The popularity signal divides by this value. Computing it once per
    ranking pass (instead of once per student-group pair) removes an
    O(students x groups^2) hot spot without changing any score.
    """
    return max((len(group.member_ids) for group in dataset.groups.values()), default=1)


def popularity(dataset: SyntheticDataset, group_id: str, max_size: int) -> float:
    return len(dataset.groups[group_id].member_ids) / max(max_size, 1)
