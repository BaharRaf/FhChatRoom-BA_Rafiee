# BA2 Experiment Summary

Total runtime: 22.3s

## Segments
- Warm students: 1500
- Cold students: 500

## Model metrics (NDCG@10 / Precision@10 / Recall@10)

| Model | All | Warm | Cold | Robustness |
|---|---|---|---|---|
| Random | 0.0509 / 0.0154 / 0.1034 | 0.0468 / 0.0105 / 0.1047 | 0.0632 / 0.0300 / 0.0995 | 1.351 |
| Popularity | 0.0643 / 0.0176 / 0.1192 | 0.0601 / 0.0119 / 0.1187 | 0.0772 / 0.0350 / 0.1207 | 1.285 |
| Content-Based | 0.1376 / 0.0404 / 0.2709 | 0.1245 / 0.0271 / 0.2713 | 0.1769 / 0.0802 / 0.2697 | 1.421 |
| Node2Vec | 0.1186 / 0.0349 / 0.2340 | 0.1096 / 0.0236 / 0.2360 | 0.1458 / 0.0688 / 0.2282 | 1.330 |
| LightGCN | 0.1003 / 0.0304 / 0.1999 | 0.0890 / 0.0197 / 0.1967 | 0.1342 / 0.0626 / 0.2095 | 1.508 |
| GraphSAGE | 0.1216 / 0.0355 / 0.2357 | 0.1095 / 0.0233 / 0.2327 | 0.1578 / 0.0720 / 0.2450 | 1.441 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = 0.023616, t p-value = 0.012018199582699219, Wilcoxon p-value = 0.015943748287760722, significant at Bonferroni-adjusted alpha: True
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = -0.036, t p-value = 0.007913224301511371, Wilcoxon p-value = 0.007955438571009398, significant at Bonferroni-adjusted alpha: True
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.019114, t p-value = 0.00023728958072150928, Wilcoxon p-value = 5.775330476537419e-05, significant at Bonferroni-adjusted alpha: True

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 20.138588, p = 0.0004689009658816673, significant disparity: True
- **graphsage_cold**: H = 13.088052, p = 0.010853474443487124, significant disparity: True
- **lightgcn_all**: H = 7.231503, p = 0.12414848965287105, significant disparity: False

