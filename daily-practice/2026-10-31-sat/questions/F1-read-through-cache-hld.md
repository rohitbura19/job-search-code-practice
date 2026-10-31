# F1. HLD: read-through cache for product pages (12 min out loud + 5 min self-review)

Feature of a big e-commerce app: the product-detail read path serves 200k RPS at peak (80% of it on 1% of products), data changes a few times a day per product, price and stock change more often. Design the caching layer between the API fleet and the catalog database.
Cover: what is cached and where (client/CDN, per-node L1, shared L2 such as Redis); keys and TTLs (negative caching for unknown ids); invalidation on update (event-driven vs TTL only); stampede protection (single flight, request coalescing, TTL jitter, serve-stale-while-revalidate); hot keys; consistency promises (read-your-writes for the seller editing the page); capacity (memory per node, hit ratio target); failure modes (L2 down, cold start after deploy) and metrics.
Use `my-designs/F1-product-page-cache.md`.
