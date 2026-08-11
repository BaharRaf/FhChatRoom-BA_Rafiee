# BA2 Experiment Summary

Total runtime: 4949.8s

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
| LightGCN | 0.0563 / 0.0167 / 0.1104 | 0.0512 / 0.0111 / 0.1107 | 0.0717 / 0.0336 / 0.1097 | 1.399 |
| GraphSAGE | 0.1126 / 0.0314 / 0.2132 | 0.1036 / 0.0216 / 0.2160 | 0.1396 / 0.0606 / 0.2050 | 1.347 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = 0.067968, t p-value = 3.354322330331592e-11, Wilcoxon p-value = 2.3272309179297533e-11, significant at Bonferroni-adjusted alpha: True
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = -0.105333, t p-value = 5.2039387974280635e-15, Wilcoxon p-value = 9.439509507618802e-15, significant at Bonferroni-adjusted alpha: True
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.027704, t p-value = 0.00010236537036321324, Wilcoxon p-value = 1.3963703719690537e-05, significant at Bonferroni-adjusted alpha: True

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 15.085397, p = 0.004527336177593671, significant disparity: True
- **graphsage_cold**: H = 9.914918, p = 0.04188552770706122, significant disparity: True
- **lightgcn_all**: H = 2.717286, p = 0.6061931819313829, significant disparity: False

