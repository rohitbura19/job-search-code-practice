> Write your answer in `my-designs/F2-typeahead.md`, then say it out loud. No tests.

### F2. Search typeahead / autocomplete for a product catalogue
User types in the search box and sees the top 8 suggestions after each keystroke. 50M daily users, about 100k suggestion requests/sec at peak, p99 < 50 ms. Suggestions ranked by popularity (and freshness: trending terms appear within the hour). Must filter offensive terms. Slightly stale data is fine.

Think about: trie with precomputed top-K per node vs inverted index vs search-engine completion suggester, offline aggregation of query logs (SQS/Kinesis -> batch job), serving store (in-memory shards / Redis / OpenSearch), CDN caching by prefix, client debounce, sharding skew (the prefix "s"), personalization layer, trending delta layer, rebuilds and hot swap.
