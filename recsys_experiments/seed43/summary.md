# BA2 Experiment Summary

Total runtime: 113.9s

## Segments
- Warm students: 1500
- Cold students: 500

## Model metrics (NDCG@10 / Precision@10 / Recall@10)

| Model | All | Warm | Cold | Robustness |
|---|---|---|---|---|
| Random | 0.0487 / 0.0146 / 0.0942 | 0.0426 / 0.0091 / 0.0907 | 0.0668 / 0.0310 / 0.1047 | 1.568 |
| Popularity | 0.0595 / 0.0165 / 0.1117 | 0.0550 / 0.0113 / 0.1133 | 0.0731 / 0.0320 / 0.1068 | 1.330 |
| Content-Based | 0.1348 / 0.0404 / 0.2693 | 0.1208 / 0.0268 / 0.2680 | 0.1768 / 0.0812 / 0.2732 | 1.464 |
| Node2Vec | 0.1173 / 0.0355 / 0.2321 | 0.1034 / 0.0227 / 0.2267 | 0.1589 / 0.0738 / 0.2483 | 1.537 |
| LightGCN | 0.1040 / 0.0286 / 0.1966 | 0.0977 / 0.0200 / 0.2000 | 0.1231 / 0.0546 / 0.1865 | 1.260 |
| GraphSAGE | 0.1271 / 0.0379 / 0.2485 | 0.1109 / 0.0242 / 0.2420 | 0.1756 / 0.0788 / 0.2682 | 1.583 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = 0.052489, t p-value = 1.579649028389984e-07, Wilcoxon p-value = 5.895306499906203e-07, significant at Bonferroni-adjusted alpha: True
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = -0.042, t p-value = 0.0011947042646334863, Wilcoxon p-value = 0.0012117805361219634, significant at Bonferroni-adjusted alpha: True
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.001241, t p-value = 0.7461026414566608, Wilcoxon p-value = 0.9798610668607133, significant at Bonferroni-adjusted alpha: False

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 2.239566, p = 0.6917923892552407, significant disparity: False
- **graphsage_cold**: H = 1.749635, p = 0.781682799301512, significant disparity: False
- **lightgcn_all**: H = 11.260047, p = 0.023791779787814004, significant disparity: True

