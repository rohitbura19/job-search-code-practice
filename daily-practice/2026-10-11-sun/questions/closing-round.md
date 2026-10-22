## Principal-engineer closing round (10 min, speak out loud)

1. In E, a consumer is slower than the producer and lag grows to 2M. Walk through diagnosis, then your three levers (scale consumers, batch writes, reduce per-message work). Where is the partition-count ceiling?
2. In F2, redo the design assuming Redis is NOT available. What do you give up?
3. In C2, the dependency graph has 10M nodes and does not fit in memory. What now?
4. In your Enphase IoT -> SQS pipeline, which single decision would you change if you rebuilt it today, and why?
