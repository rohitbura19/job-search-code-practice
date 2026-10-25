# Reference solutions (open only after trying)

Only files you must write are here (given types stay in `src/main/java`). The Java files mirror `src/main/java`; to check them, copy `answers/java/*` over `src/main/java/` in a scratch copy and run `mvn test`.
The Spring solution is under `e-spring-coupons/src/` (copy over the project's `src/`). Each file's header comment has the idea and the complexity.

| Item | File | Time | Space |
|---|---|---|---|
| A1 | java/.../a1/Intersect.java | O(n + m) (+ sort of the result) | O(min(n, m)); sorted variant O(1) |
| A2 | java/.../a2/MinStack.java | O(1) per op | O(n) |
| B1 | java/.../b1/BookingStats.java | O(n) per method, sorted maps O(n log n) | O(n) |
| C1 | java/.../c1/Prefix.java | O(n) both | O(1) / O(n) |
| C2 | java/.../c2/Accounts.java | O(E alpha(A) + E log E) | O(E) |
| D1 | java/.../d1/Median.java | O(log min(n, m)) | O(1) |
| E | e-spring-coupons/src/... | - | - |
| G | java/.../g/{Base62,InMemoryUrlStore,DefaultUrlShortener}.java | O(1) per call (store O(1)) | O(links) |
| H1 | java/.../h1/H1Predictions.java | - | the reasoning is in the header |
| H2 | java/.../h2/*Pricing.java | O(1) per call | O(distinct orders) for the cache |

F1 / F2 (HLD) anchors.
**F1:** write path: API -> id block allocator (each app node holds a block of ids) -> Base62 (optionally scrambled) -> key-value store (`code -> url, owner, expiresAt`) with a unique key as the guard for custom aliases; read path: edge cache -> in-process LRU -> store replica; `302` redirect and an async click event to a log; analytics consumers aggregate per link per hour into a columnar/OLAP store; expiry is checked on read and cleaned lazily + by a TTL index; store sharded by code (hash), replicated across regions with the id blocks partitioned per region so codes never collide.
**F2:** gateway nodes hold the connections and refresh `presence:{user}:{device}` in Redis with a 60 s TTL on each heartbeat (about 30M / 30 s = 1M writes/s sharded by user); a keyspace-expiry or sweeper emits "went offline"; subscribers register the contacts on screen with their gateway, which receives changes from a pub/sub channel per user and pushes a debounced update; "last seen" lives in a durable table updated on disconnect/expiry; celebrities are read on demand with a few seconds of cache instead of fan-out.
