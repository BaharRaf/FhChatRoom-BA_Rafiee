# BA2 Experiment Summary

Total runtime: 2086.2s

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
| LightGCN | 0.0539 / 0.0160 / 0.1051 | 0.0489 / 0.0105 / 0.1047 | 0.0687 / 0.0326 / 0.1065 | 1.406 |
| GraphSAGE | 0.1182 / 0.0340 / 0.2287 | 0.1096 / 0.0231 / 0.2313 | 0.1441 / 0.0666 / 0.2208 | 1.316 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = 0.07539, t p-value = 1.1398571564851412e-13, Wilcoxon p-value = 1.976439987358715e-14, significant at Bonferroni-adjusted alpha: True
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = -0.126667, t p-value = 1.0097315530922734e-20, Wilcoxon p-value = 3.399889554922872e-20, significant at Bonferroni-adjusted alpha: True
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.023199, t p-value = 8.386300383836388e-05, Wilcoxon p-value = 0.00020171601601940586, significant at Bonferroni-adjusted alpha: True

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 40.852141, p = 2.884116624793352e-08, significant disparity: True
- **graphsage_cold**: H = 13.449847, p = 0.009274638459381864, significant disparity: True
- **lightgcn_all**: H = 4.435605, p = 0.3502514965790063, significant disparity: False

