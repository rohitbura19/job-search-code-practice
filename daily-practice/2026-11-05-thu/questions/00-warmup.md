# Warm-up (10 min, blank page, no IDE)

1. Prefix sums: why does "subarray sum equals k" become a lookup of `prefix - k` in a map, and why must the map be seeded with `{0: 1}`? What breaks if you seed it after the loop starts?
2. Median of two sorted arrays: what does it mean for a partition `(i, j)` to be valid, and why do you binary search on the SHORTER array?
3. Multi-source BFS: why is one BFS started from all sources at once correct (and faster) compared with one BFS per source?
4. A Kafka consumer processes a message, crashes before committing the offset, restarts. What does the broker redeliver, and what must your handler guarantee? Why mark the event id as processed in the same DB transaction as the state change?
