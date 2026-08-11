# BA2 Experiment Summary

Total runtime: 238.0s

## Segments
- Warm students: 1500
- Cold students: 500

## Model metrics (NDCG@10 / Precision@10 / Recall@10)

| Model | All | Warm | Cold | Robustness |
|---|---|---|---|---|
| Random | 0.0466 / 0.0143 / 0.0937 | 0.0415 / 0.0092 / 0.0920 | 0.0619 / 0.0296 / 0.0988 | 1.490 |
| Popularity | 0.0538 / 0.0159 / 0.1118 | 0.0519 / 0.0117 / 0.1173 | 0.0592 / 0.0284 / 0.0952 | 1.140 |
| Content-Based | 0.1415 / 0.0406 / 0.2738 | 0.1327 / 0.0278 / 0.2780 | 0.1681 / 0.0792 / 0.2613 | 1.267 |
| Node2Vec | 0.1214 / 0.0358 / 0.2490 | 0.1145 / 0.0259 / 0.2587 | 0.1419 / 0.0658 / 0.2202 | 1.239 |
| LightGCN | 0.1030 / 0.0314 / 0.2097 | 0.0927 / 0.0210 / 0.2100 | 0.1340 / 0.0624 / 0.2088 | 1.445 |
| GraphSAGE | 0.1186 / 0.0338 / 0.2270 | 0.1101 / 0.0229 / 0.2293 | 0.1441 / 0.0666 / 0.2202 | 1.308 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = 0.010093, t p-value = 0.28020886540423756, Wilcoxon p-value = 0.1817904486079267, significant at Bonferroni-adjusted alpha: False
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = -0.019333, t p-value = 0.14559883399617232, Wilcoxon p-value = 0.14553966595850681, significant at Bonferroni-adjusted alpha: False
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.024037, t p-value = 4.5357644435452375e-05, Wilcoxon p-value = 7.003260569876153e-05, significant at Bonferroni-adjusted alpha: True

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 44.462746, p = 5.14176581635109e-09, significant disparity: True
- **graphsage_cold**: H = 16.831022, p = 0.002084653500020449, significant disparity: True
- **lightgcn_all**: H = 7.731341, p = 0.1019303172918721, significant disparity: False

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
