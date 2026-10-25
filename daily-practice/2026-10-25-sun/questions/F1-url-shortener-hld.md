# F1. HLD: URL shortener with click analytics (12 min out loud + 5 min self-review)

A shortener for a large app (in-app share links and SMS): create a short link (optionally with a custom alias and an expiry), redirect on click, show click counts to the owner. 100M new links per month, 10:1 read:write at peak, redirects must be fast and survive a regional outage.

Cover: requirements and non-goals (abuse/phishing scan? vanity domains?), scale estimates (writes/s, redirects/s, storage for 5 years, code length for that many links), API (create, redirect `301` vs `302`, stats; idempotency of create), code generation (counter + Base62 vs hash vs random; collisions; custom aliases), data model and store choice, cache layer and hot links, click counting without a write per click, expiry, abuse and rate limits, failure modes.
