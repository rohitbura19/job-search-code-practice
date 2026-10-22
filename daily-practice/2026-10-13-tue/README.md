# Tue 13 Oct 2026 - practice

## Order for today
1. **Minimum set (about 80 min):** `questions/00-warmup.md` (10 min) -> **A1** (15 min) -> **C1** (15 min) -> **E** (25 min, Spring project: validation, cache proxy, RestClient) -> **H1** predictions (10 min) -> closing question 1 out loud (5 min).
2. Extended set: A2, B1, C2, D1, F1, F2, G, H2.

| Item | Question | Where you write code / answer | Test |
|---|---|---|---|
| Warm-up | questions/00-warmup.md | paper | - |
| A1 | questions/A1-diameter-of-binary-tree.md | a1/DiameterOfBinaryTree.java | DiameterOfBinaryTreeTest |
| A2 | questions/A2-best-time-to-buy-sell-stock.md | a2/StockProfit.java | StockProfitTest |
| B1 | questions/B1-order-analytics-lru.md | b1/OrderAnalytics.java, b1/Lru.java | OrderAnalyticsTest |
| C1 | questions/C1-longest-substring-no-repeat.md | c1/LongestSubstring.java | LongestSubstringTest |
| C2 | questions/C2-combination-sum.md | c2/CombinationSum.java | CombinationSumTest |
| D1 | questions/D1-trapping-rain-water.md | d1/TrapRainWater.java | TrapRainWaterTest |
| E | questions/E-tariff-estimate-service.md | e-spring-tariff-estimate/ | EstimateControllerTest, EstimateServiceTest, TariffLookupCacheTest, TariffUpdatedListenerTest, FxClientTest, FxPropsTest |
| F1 | questions/F1-ota-firmware-rollout-hld.md | my-designs/F1-ota-rollout.md | say it out loud |
| F2 | questions/F2-typeahead-hld.md | my-designs/F2-typeahead.md | say it out loud |
| G | questions/G-firmware-rollout-lld.md | my-designs/G-classes-sketch.md, then g/DeviceState, HwRevRule, NotChargingRule, Eligibility, FailureRateAbort, Campaign, StatusConsumer | RolloutTest |
| H1 | questions/H1-try-finally-string-pool.md | h1/H1Predictions.java (read H1Snippets.java) | H1PredictionsTest |
| H2 | questions/H2-immutability-dip.md | h2/Booking.java, BookingRecord.java, OrderService.java | BookingTest, OrderServiceTest |
| Close | questions/closing-round.md | out loud | - |

Code paths are under `src/main/java/practice/d20261013/`. Run the main tests with `mvn test`; the Spring project has its own pom (`cd e-spring-tariff-estimate && mvn test`).

## Score card

| Section | Done? | Tests green without hints? | Minutes | Weak point to revisit |
|---|---|---|---|---|
| Warm-up | | - | | |
| A1 / A2 | | | | |
| B1 | | | | |
| C1 / C2 | | | | |
| D1 | | | | |
| E | | | | |
| F1 / F2 | | - | | |
| G | | | | |
| H1 / H2 | | | | |

Minutes actually spent: ____
