# F1 - URL shortener with click analytics (HLD, 12 min + 5 review)

`POST /links` creates a short code for a long URL (optional custom alias, optional expiry); `GET /{code}` redirects (301/302) and counts the click.

Requirements: 100M new links/month, 100:1 read:write (about 4k redirects/s average, 40k peak), redirect p99 < 20 ms, links live for years, per-link click counts and a daily time series (eventually consistent is fine), custom aliases must be unique, expired/deleted links return 404/410, abuse protection.
Discuss: code generation (counter + Base62 vs random vs hash; collisions; guessability), storage (key-value store, sharding by code), caching and CDN (301 vs 302 trade-off for analytics), the click pipeline (Kafka, aggregation, hot-link counters), expiry and cleanup, rate limiting and malicious URLs, multi-region.
