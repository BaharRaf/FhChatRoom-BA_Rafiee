"""Cross-interest probe: does the recommender surface a *non-academic*,
cross-field leisure group to non-members?

This is a deliberate stress test of the thesis logic. The recommender scores a
candidate group for a student with
    0.8 * Relevance + 0.15 * Serendipity + 0.05 * Popularity,
where Relevance = 0.65*embedding-cosine + 0.2*topic-sim + 0.1*study-path-affinity
+ 0.05*semester-proximity, and Serendipity = TopicSim*(1-MemberSim).

A leisure group ("Weekend Hiking & Outdoors") whose members span *all* study
paths has:
  * low study-path affinity for any single student (members are mixed),
  * low topic similarity to a student's *academic* topic profile (its topics
    are hiking/outdoors, not the student's coursework),
  * therefore low serendipity (serendipity needs topic overlap), and
  * whatever GraphSAGE embedding similarity arises purely from *graph
    structure* -- shared peers / shared academic-room membership with the
    group's same-field members.

So this probe asks an honest question: with the academic/topic signals muted,
does the inductive graph structure alone still connect non-members to a
cross-field social group? It reports, per study path, how often the leisure
group enters non-members' top-10 and its mean rank, against a same-size
single-field control group.

Usage:
    python3 -m recsys.run_cross_interest_probe
"""

from __future__ import annotations

import argparse
import statistics


from recsys.graphsage_prep import GraphSAGEConfig
from recsys.graphsage_prep import prepare_graphsage_training_data
from recsys.graphsage_train import GraphSAGETrainConfig
from recsys.graphsage_train import build_graphsage_recommendations
from recsys.graphsage_train import train_graphsage_embeddings
from recsys.hin import build_hin
from recsys.models import DatasetConfig
from recsys.models import Message
from recsys.models import StudyGroup
from recsys.synthetic_data import generate_synthetic_dataset

LEISURE_ID = "sm_leisure_hiking"
CONTROL_ID = "sm_control_singlefield"
LEISURE_TEXT = (
    "weekend hiking trip outdoors mountains hike trail nature camping "
    "social meetup leisure trip weekend outing"
)


def _inject_groups(dataset, rng, members_per_field: int, control_field: str):
    """Adds a cross-field leisure group and a single-field control group."""
    by_field: dict[str, list[str]] = {}
    for sid, st in dataset.students.items():
        by_field.setdefault(st.study_path, []).append(sid)
    for field in by_field:
        by_field[field].sort()

    # cross-field leisure group: members_per_field students from EVERY field
    leisure_members: list[str] = []
    for field, ids in sorted(by_field.items()):
        leisure_members.extend(rng.sample(ids, min(members_per_field, len(ids))))
    # single-field control: same total size, all from one field
    n = len(leisure_members)
    control_pool = [s for s in by_field[control_field] if s not in leisure_members]
    control_members = rng.sample(control_pool, min(n, len(control_pool)))

    dataset.groups[LEISURE_ID] = StudyGroup(
        id=LEISURE_ID,
        name="Weekend Hiking & Outdoors Club",
        category="Leisure",
        description="A cross-faculty club for weekend hikes, trips and outdoor meetups.",
        primary_study_path="",  # genuinely cross-field
        topic_tags=["hiking", "outdoor", "trip"],
        member_ids=list(leisure_members),
        is_student_made=True,
    )
    dataset.groups[CONTROL_ID] = StudyGroup(
        id=CONTROL_ID,
        name=f"{control_field.split()[0]} Peer Circle (control)",
        category="Study Group",
        description=f"A single-field study group for {control_field} students.",
        primary_study_path=control_field,
        topic_tags=dataset.groups[next(iter(dataset.groups))].topic_tags[:1] or ["study"],
        member_ids=list(control_members),
        is_student_made=True,
    )
    for sid in leisure_members:
        dataset.students[sid].joined_group_ids.append(LEISURE_ID)
    for sid in control_members:
        dataset.students[sid].joined_group_ids.append(CONTROL_ID)

    # give the leisure group leisure-themed messages so it has a topic vector
    base_day = max((m.day for m in dataset.messages), default=1)
    for i, sid in enumerate(leisure_members):
        dataset.messages.append(
            Message(
                id=f"msg-leisure-{i}",
                sender_id=sid,
                group_id=LEISURE_ID,
                text=LEISURE_TEXT,
                day=base_day,
                reaction_count=1,
            )
        )
    return set(leisure_members), set(control_members), by_field


def _rank_of(detailed_items, group_id):
    for rank, item in enumerate(detailed_items, 1):
        if str(item["groupId"]) == group_id:
            return rank, item
    return None, None


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--students", type=int, default=1000)
    parser.add_argument("--groups", type=int, default=60)
    parser.add_argument("--topics", type=int, default=50)
    parser.add_argument("--epochs", type=int, default=20)
    parser.add_argument("--seed", type=int, default=42)
    parser.add_argument("--members-per-field", type=int, default=8)
    parser.add_argument("--top-k", type=int, default=10)
    args = parser.parse_args()

    import random

    rng = random.Random(args.seed)
    dataset = generate_synthetic_dataset(
        config=DatasetConfig(
            num_students=args.students,
            num_groups=args.groups,
            num_topics=args.topics,
            messages_per_day=120,
            num_days=14,
        ),
        seed=args.seed,
    )
    control_field = "Health Care and Nursing"
    leisure_members, control_members, by_field = _inject_groups(
        dataset, rng, args.members_per_field, control_field
    )
    print(
        f"Injected '{dataset.groups[LEISURE_ID].name}' with {len(leisure_members)} members "
        f"across {len(by_field)} study paths, and a same-size single-field control "
        f"({len(control_members)} {control_field} members).\n"
    )

    hin = build_hin(dataset, max_topics=args.topics)
    leisure_in_topics = any(
        t in hin.selected_topics for t in ("hiking", "outdoor", "trip", "weekend")
    )
    print(f"Did leisure topics survive into the {args.topics}-topic TF-IDF vocabulary? "
          f"{'yes' if leisure_in_topics else 'no (academic tokens dominate)'}\n")

    prep = prepare_graphsage_training_data(dataset=dataset, hin=hin, config=GraphSAGEConfig(seed=args.seed))
    result = train_graphsage_embeddings(prep=prep, config=GraphSAGETrainConfig(epochs=args.epochs, seed=args.seed))
    detailed = build_graphsage_recommendations(dataset, hin, result, top_k=args.top_k)

    print(f"Per study path: does the cross-field leisure group reach non-members' top-{args.top_k}?")
    print(f"{'study path':<42}{'leisure in top-K':>18}{'mean rank':>12}{'control in top-K':>18}")
    for field, ids in sorted(by_field.items()):
        non_members = [s for s in ids if s not in leisure_members]
        l_ranks, c_hits, l_hits = [], 0, 0
        for sid in non_members:
            items = detailed.get(sid, [])
            lr, _ = _rank_of(items, LEISURE_ID)
            if lr:
                l_hits += 1
                l_ranks.append(lr)
            cr, _ = _rank_of(items, CONTROL_ID)
            if cr and sid not in control_members:
                c_hits += 1
        share = l_hits / len(non_members) if non_members else 0.0
        cshare = c_hits / len(non_members) if non_members else 0.0
        mr = f"{statistics.mean(l_ranks):.1f}" if l_ranks else "--"
        print(f"{field[:40]:<42}{share:>17.0%}{mr:>12}{cshare:>17.0%}")

    # example score breakdown for one non-member who DID get the leisure group
    print("\nExample score breakdown (a non-member who received the leisure group):")
    for sid in sorted(set().union(*by_field.values())):
        if sid in leisure_members:
            continue
        lr, item = _rank_of(detailed.get(sid, []), LEISURE_ID)
        if lr:
            b = item["breakdown"]
            print(
                f"  {sid} ({dataset.students[sid].study_path[:24]}): leisure rank #{lr}, "
                f"score={item['score']:.3f} "
                f"[embed/relevance={b['relevance']:.2f} topic={b['topicSimilarity']:.2f} "
                f"path={b['studyPathAffinity']:.2f} serendipity={b['serendipity']:.2f} "
                f"popularity={b['popularity']:.2f}]"
            )
            break
    else:
        print("  (no non-member received the leisure group in their top-K)")


if __name__ == "__main__":
    main()
