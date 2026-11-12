# F1 - Search typeahead / autocomplete for an e-commerce app (HLD, 12 min out loud + 5 min self-review) - `my-designs/F1-typeahead.md`

Design the "suggestions as you type" box of a shop's search bar: after 1-2 characters the client shows 5-10 completions. Think: 50k suggestion requests/s at peak, p99 < 50 ms end to end, queries are ranked by recent popularity (with personalisation as a stretch), offensive/blocked terms must never appear, new trending queries should show up within minutes, and the typed text can be a typo.
Cover: API, how suggestions are stored and served (trie vs prefix keys in a KV store vs search engine), the offline/streaming pipeline that builds them from the query log, caching (CDN, client debounce), freshness vs cost, blocklist and abuse, failure modes.
