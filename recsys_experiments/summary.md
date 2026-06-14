# BA2 Experiment Summary

Total runtime: 188.9s

## Segments
- Warm students: 1500
- Cold students: 500

## Model metrics (NDCG@10 / Precision@10 / Recall@10)

| Model | All | Warm | Cold | Robustness |
|---|---|---|---|---|
| Random | 0.0466 / 0.0143 / 0.0937 | 0.0415 / 0.0092 / 0.0920 | 0.0619 / 0.0296 / 0.0988 | 1.490 |
| Popularity | 0.0538 / 0.0159 / 0.1118 | 0.0519 / 0.0117 / 0.1173 | 0.0592 / 0.0284 / 0.0952 | 1.140 |
| Content-Based | 0.1418 / 0.0408 / 0.2757 | 0.1337 / 0.0280 / 0.2800 | 0.1660 / 0.0794 / 0.2630 | 1.241 |
| Node2Vec | 0.0619 / 0.0170 / 0.1228 | 0.0627 / 0.0132 / 0.1320 | 0.0592 / 0.0284 / 0.0952 | 0.944 |
| LightGCN | 0.0497 / 0.0152 / 0.1024 | 0.0458 / 0.0103 / 0.1033 | 0.0615 / 0.0300 / 0.0995 | 1.341 |
| GraphSAGE | 0.1169 / 0.0337 / 0.2196 | 0.1055 / 0.0216 / 0.2160 | 0.1510 / 0.0700 / 0.2303 | 1.431 |

## Hypothesis tests
- **H1_graphsage_vs_lightgcn_cold_ndcg**: mean diff = 0.089514, t p-value = 2.9221076209435232e-21, Wilcoxon p-value = 2.563160504961684e-20, significant at Bonferroni-adjusted alpha: True
- **H2_lightgcn_vs_graphsage_warm_recall**: mean diff = -0.112667, t p-value = 1.8964172542454936e-16, Wilcoxon p-value = 3.938323947154821e-16, significant at Bonferroni-adjusted alpha: True
- **supplementary_graphsage_vs_content_cold_ndcg**: mean diff = -0.014986, t p-value = 0.006171895171862395, Wilcoxon p-value = 0.005469614953877142, significant at Bonferroni-adjusted alpha: True

## Fairness (Kruskal-Wallis)
- **graphsage_all**: H = 11.824065, p = 0.018708656232877457, significant disparity: True
- **graphsage_cold**: H = 4.743846, p = 0.3146042552253703, significant disparity: False
- **lightgcn_all**: H = 1.656266, p = 0.7986456821178349, significant disparity: False

## Privacy-utility sweep (all-users NDCG@10)

| epsilon | tiered | uniform | gradient |
|---|---|---|---|
| 0.25 | 0.1150 | 0.1150 | 0.1087 |
| 0.5 | 0.1150 | 0.1150 | 0.1103 |
| 1.0 | 0.1150 | 0.1150 | 0.1127 |
| 3.0 | 0.1149 | 0.1150 | 0.1145 |
| 5.0 | 0.1149 | 0.1150 | 0.1159 |
| 8.0 | 0.1146 | 0.1150 | 0.1159 |
| 10.0 | 0.1146 | 0.1150 | 0.1151 |
