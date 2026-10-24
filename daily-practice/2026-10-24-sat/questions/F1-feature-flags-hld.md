# F1. HLD: feature flag service (12 min out loud + 5 min self-review)

A feature flag service for 300 microservices and 3 mobile apps: engineers create flags, change rollout percentages and targeting (allow-list of users, per-country), and kill a flag instantly. Services evaluate flags on the hot path.

Cover: requirements and non-goals (experiments/analytics?), scale (evaluations per second, flags count, size of one flag set, how many changes per day), API (admin CRUD with audit and versioning; the SDK pull/stream contract), data model, how evaluation stays local and fast (SDK caches the whole ruleset and evaluates in-process vs a remote `isEnabled` call), propagation latency of a kill switch (polling + ETag vs SSE/long-poll push), sticky bucketing (hash of flag + user id) and what a ramp from 10% to 20% must guarantee, consistency during a rollout (two services see different versions), failure modes (control plane down: serve the last known good, bundled defaults at startup), safety (approval flow, per-environment, scheduled changes, flag cleanup/expiry), trade-offs.

Fill in `my-designs/F1-feature-flags.md`.
