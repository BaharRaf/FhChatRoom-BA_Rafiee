# BA2 Experiment Summary

Total runtime: 91.7s

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
| GraphSAGE | 0.1170 / 0.0336 / 0.2237 | 0.1070 / 0.0224 / 0.2240 | 0.1471 / 0.0670 / 0.2228 | 1.375 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = 0.013083, t p-value = 0.16036992787195734, Wilcoxon p-value = 0.09611424219576846, significant at Bonferroni-adjusted alpha: False
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = -0.014, t p-value = 0.2896096983136414, Wilcoxon p-value = 0.2894586805670044, significant at Bonferroni-adjusted alpha: False
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.021047, t p-value = 0.00040324831256125063, Wilcoxon p-value = 0.0018698231220074019, significant at Bonferroni-adjusted alpha: True

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 34.73159, p = 5.274007880903888e-07, significant disparity: True
- **graphsage_cold**: H = 15.270232, p = 0.004172336663176798, significant disparity: True
- **lightgcn_all**: H = 7.731341, p = 0.1019303172918721, significant disparity: False

