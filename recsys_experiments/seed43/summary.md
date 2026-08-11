# BA2 Experiment Summary

Total runtime: 131.2s

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
| GraphSAGE | 0.1113 / 0.0331 / 0.2191 | 0.0980 / 0.0216 / 0.2160 | 0.1512 / 0.0676 / 0.2283 | 1.543 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = 0.028144, t p-value = 0.004968036638922483, Wilcoxon p-value = 0.007166447399871549, significant at Bonferroni-adjusted alpha: True
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = -0.016, t p-value = 0.22556071917595114, Wilcoxon p-value = 0.22544231699451311, significant at Bonferroni-adjusted alpha: False
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.025586, t p-value = 2.881056571797218e-05, Wilcoxon p-value = 3.6349839653857904e-05, significant at Bonferroni-adjusted alpha: True

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 20.905092, p = 0.00033068843018630226, significant disparity: True
- **graphsage_cold**: H = 15.230449, p = 0.004246358593272414, significant disparity: True
- **lightgcn_all**: H = 11.260047, p = 0.023791779787814004, significant disparity: True

