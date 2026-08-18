# BA2 Experiment Summary

Total runtime: 113.9s

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
| GraphSAGE | 0.0999 / 0.0296 / 0.1952 | 0.0894 / 0.0193 / 0.1933 | 0.1314 / 0.0604 / 0.2007 | 1.469 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = -0.00261, t p-value = 0.7688454940488818, Wilcoxon p-value = 0.6100802174289472, significant at Bonferroni-adjusted alpha: False
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = 0.016667, t p-value = 0.2038966181564726, Wilcoxon p-value = 0.20379218683695377, significant at Bonferroni-adjusted alpha: False
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.036739, t p-value = 3.521739856463087e-09, Wilcoxon p-value = 9.372637613363104e-10, significant at Bonferroni-adjusted alpha: True

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 7.799447, p = 0.09920719821624621, significant disparity: False
- **graphsage_cold**: H = 2.303611, p = 0.6801117017313667, significant disparity: False
- **lightgcn_all**: H = 7.731341, p = 0.1019303172918721, significant disparity: False

