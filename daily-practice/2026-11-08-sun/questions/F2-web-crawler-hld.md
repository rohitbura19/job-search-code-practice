# F2 - Web crawler (HLD, 12 min + 5 review)

Crawl 1 billion pages per month for a search index. Requirements: polite (per-host rate limits, robots.txt), no duplicate fetches (URL and near-duplicate content), prioritise fresh/important pages, re-crawl on a schedule, tolerate dead hosts and spider traps, horizontally scalable, observable.
Discuss: URL frontier (priority queues + one back queue per host), host-to-worker partitioning, DNS caching, URL normalisation and dedupe (Bloom filter + persistent set), content fingerprints (SimHash), crawl-delay scheduling, trap detection (depth/URL length/path patterns), storage of raw pages and metadata, capacity maths (pages/s, bandwidth, storage), failure handling and retries.
