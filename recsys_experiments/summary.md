# BA2 Experiment Summary

Total runtime: 241.4s

## Segments
- Warm students: 1500
- Cold students: 500

## Model metrics (NDCG@10 / Precision@10 / Recall@10)

| Model | All | Warm | Cold | Robustness |
|---|---|---|---|---|
| Random | 0.0466 / 0.0143 / 0.0937 | 0.0415 / 0.0092 / 0.0920 | 0.0619 / 0.0296 / 0.0988 | 1.490 |
| Popularity | 0.0538 / 0.0159 / 0.1118 | 0.0519 / 0.0117 / 0.1173 | 0.0592 / 0.0284 / 0.0952 | 1.140 |
| Content-Based | 0.1405 / 0.0403 / 0.2707 | 0.1315 / 0.0275 / 0.2747 | 0.1673 / 0.0786 / 0.2590 | 1.272 |
| Node2Vec | 0.1214 / 0.0358 / 0.2490 | 0.1145 / 0.0259 / 0.2587 | 0.1419 / 0.0658 / 0.2202 | 1.239 |
| LightGCN | 0.0563 / 0.0167 / 0.1104 | 0.0512 / 0.0111 / 0.1107 | 0.0714 / 0.0336 / 0.1097 | 1.393 |
| GraphSAGE | 0.1186 / 0.0338 / 0.2270 | 0.1101 / 0.0229 / 0.2293 | 0.1441 / 0.0666 / 0.2202 | 1.308 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = 0.072713, t p-value = 5.253966445045448e-13, Wilcoxon p-value = 6.476182226861287e-13, significant at Bonferroni-adjusted alpha: True
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = -0.118667, t p-value = 3.692056621537208e-18, Wilcoxon p-value = 9.169196816138554e-18, significant at Bonferroni-adjusted alpha: True
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.023255, t p-value = 6.833569053210978e-05, Wilcoxon p-value = 7.164322523228316e-05, significant at Bonferroni-adjusted alpha: True

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 44.462746, p = 5.14176581635109e-09, significant disparity: True
- **graphsage_cold**: H = 16.831022, p = 0.002084653500020449, significant disparity: True
- **lightgcn_all**: H = 2.666782, p = 0.6150397856426006, significant disparity: False

## Privacy-utility sweep (all-users NDCG@10)

| epsilon | tiered | uniform | gradient |
|---|---|---|---|
| 0.25 | 0.1204 | 0.1204 | 0.1189 |
| 0.5 | 0.1204 | 0.1204 | 0.1202 |
| 1.0 | 0.1204 | 0.1204 | 0.1281 |
| 3.0 | 0.1204 | 0.1203 | 0.1217 |
| 5.0 | 0.1202 | 0.1204 | 0.1192 |
| 8.0 | 0.1201 | 0.1205 | 0.1151 |
| 10.0 | 0.1201 | 0.1203 | 0.1155 |
