# Answers: F1 and F2 (HLD) and the principal-level discussion

(Spoilers.)

**Reference — F1**
```
Operator -> Rollout Service (Spring Boot) -> campaigns + device_state (DynamoDB / SQL)
                 | picks next wave, applies eligibility rules, creates one job per device
                 v
        AWS IoT Core (MQTT)  --notify {jobId, version, url, sha256, sig}-->  Charger
Charger --GET (resumable, Range)--> CloudFront --> S3 (signed image)
Charger --status (DOWNLOADING/INSTALLING/SUCCEEDED/FAILED)--> IoT Core rule --> SQS --> Status Consumer
                 --> device_state update + sharded counters --> Wave Evaluator (abort / promote)
```
- **Control vs data plane**: MQTT carries a tiny job document; the 40 MB goes over HTTPS from a CDN with `Range` requests so the charger resumes after a drop. S3 + CloudFront absorbs 20 TB without touching your services. (AWS IoT Jobs gives rollout rates, abort criteria and timeouts as a managed option; know what it does so you can say "build vs buy".)
- **Device state machine**: QUEUED -> NOTIFIED -> DOWNLOADING -> INSTALLING -> SUCCEEDED, with FAILED / TIMED_OUT / SKIPPED as exits. Only forward moves are legal, so duplicate or out-of-order MQTT messages are ignored (idempotent).
- **Eligibility** evaluated at send time and again on the device: compatible hw revision, version path allowed (e.g. must go via 2.4 first), not charging (the charger defers until the session ends and idle), battery/grid-health conditions.
- **Waves**: wave N+1 starts only after wave N has soaked for X hours AND failure rate is under 2% with a minimum sample (say 200 devices) so 1 failure out of 3 does not abort at 33%. Rate-limit notifications (e.g. 2k devices/min) with random jitter so there is no thundering herd on the CDN.
- **Security and safety**: image signed with a private key held in KMS/Signer; the device verifies the signature and checksum before flashing; A/B partitions so a failed boot reverts to the old image; the device reports the version after reboot, which is the real success signal.
- **Offline devices**: the job stays pending with an expiry (e.g. 14 days); on reconnect the device fetches pending jobs (IoT shadow / jobs API). If a newer firmware has been released meanwhile, supersede the job and skip straight to the latest.
- **Counters**: per-campaign counters updated by 100k events/min become a hot partition. Use sharded counters (write to `campaign#shard-{0..N}` and sum on read) or aggregate in the consumer with micro-batches.
- **Metrics**: devices per state per wave, failure rate by hw revision and by site, time-to-install p50/p95, CDN egress and error rate, devices stuck in NOTIFIED beyond SLA.

**Reference — F2**
```
Search logs -> Kinesis/SQS -> hourly batch (Spark/Athena) : count queries per prefix, apply filters
     -> builds prefix -> top-8 map (trie snapshot) -> S3 (versioned) -> loaded by Suggest Service nodes
Client (debounce 150 ms) -> CDN (cache key = prefix, TTL 60 s) -> Suggest Service (in-memory trie) 
                                                            + Trending Redis (last 1 h delta) merged on read
```
- Data structure: a trie where each node stores its precomputed top-K suggestions (ids + score). Lookup is O(len(prefix)) and K items returned with no subtree traversal. Memory: store only prefixes up to length ~10 and only queries seen more than N times.
- Freshness: the full rebuild is hourly/daily and swapped atomically (build new snapshot, load, flip pointer). A small "trending" structure (Redis sorted set, 1 h sliding window) is merged at read time so new terms show up fast.
- Caching: short prefixes ("a", "ap") have huge hit rates, so the CDN serves most traffic; only long-tail prefixes reach the service. Client debounce plus cancelling stale in-flight requests cuts load 3 to 5x.
- Sharding: by first two characters leads to skew ("sa" vs "xq"). Prefer replicating the whole snapshot (a 5 to 20 GB snapshot fits in memory on a node) over sharding; shard by hash of prefix only if it outgrows memory.
- Filtering: offensive/blocked terms removed at build time plus a runtime denylist. Typos: fuzzy fallback via an edit-distance index only when exact prefix returns fewer than K.
- Failure: stale snapshot is acceptable; if the service is down the search box still works without suggestions (degrade gracefully). Keep the previous snapshot for rollback.
- Metrics: suggestion latency p99, CDN hit ratio, click-through rate of suggestions (the real quality signal), snapshot age.

**Principal-level discussion: make F1 more efficient** (questions are in `questions/F1-ota-firmware-rollout-hld.md`; the sheet gives these prompts without a separate written answer, the F1 reference above is the basis: delta updates and their cost, push vs pull, blast radius by hardware revision and region, thundering herd controls, pause vs abort vs roll back.)
