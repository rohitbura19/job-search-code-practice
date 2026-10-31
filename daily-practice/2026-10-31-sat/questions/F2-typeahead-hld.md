# F2. HLD: search typeahead suggestions (12 min out loud + 5 min self-review)

Feature of the same app: as the user types, show 8 suggestions within 100 ms p99. 50k QPS of prefix lookups, a catalogue of 50M products plus popular queries from the last 7 days.
Cover: data structures (trie vs sorted prefix index vs precomputed top-k per prefix), how top-k is ranked (popularity decayed over time, personalisation as a re-rank), how the index is built and refreshed (batch + incremental), sharding by prefix and the skew of short prefixes, client behaviour (debounce, cache, cancel in-flight), typos, blocked terms, multi-language, failure modes and metrics (suggestion click-through, zero-result rate).
Use `my-designs/F2-typeahead.md`.
