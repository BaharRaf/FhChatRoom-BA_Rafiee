# BA2 Experiment Summary

Total runtime: 937.3s

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
| GraphSAGE | 0.1123 / 0.0332 / 0.2174 | 0.1011 / 0.0215 / 0.2147 | 0.1461 / 0.0684 / 0.2255 | 1.446 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = 0.012139, t p-value = 0.18189261411673224, Wilcoxon p-value = 0.30464637885332857, significant at Bonferroni-adjusted alpha: False
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = -0.004667, t p-value = 0.725474724028115, Wilcoxon p-value = 0.7253483452862952, significant at Bonferroni-adjusted alpha: False
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.021991, t p-value = 1.4020860490815688e-05, Wilcoxon p-value = 3.883719269313491e-06, significant at Bonferroni-adjusted alpha: True

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 13.197275, p = 0.01035103659333345, significant disparity: True
- **graphsage_cold**: H = 5.423529, p = 0.24653358059396238, significant disparity: False
- **lightgcn_all**: H = 7.731341, p = 0.1019303172918721, significant disparity: False

## Privacy-utility sweep (all-users NDCG@10)

| epsilon | tiered | uniform | gradient |
|---|---|---|---|
| 0.25 | 0.1272 | 0.1272 | 0.1046 |
| 0.5 | 0.1272 | 0.1272 | 0.1079 |
| 1.0 | 0.1272 | 0.1272 | 0.1120 |
| 3.0 | 0.1275 | 0.1273 | 0.1121 |
| 5.0 | 0.1276 | 0.1272 | 0.1154 |
| 8.0 | 0.1277 | 0.1273 | 0.1139 |
| 10.0 | 0.1276 | 0.1273 | 0.1125 |
