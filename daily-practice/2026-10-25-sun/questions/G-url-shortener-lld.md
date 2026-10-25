> Code: `src/main/java/practice/d20261025/g/` (`ShortUrl`, `UrlStore`, `IdGenerator`, `UrlShortener`, `AliasTakenException` are given; implement `Base62`, `InMemoryUrlStore`, `DefaultUrlShortener`). Test: `DefaultUrlShortenerTest`.

# G. LLD of F1: the shortener core (sketch 10 min, code 25 min)

Sketch in `my-designs/G-classes-sketch.md` first (immutable types, who owns uniqueness, where the clock comes from), then implement.

Contract (Javadoc of `UrlShortener`): URLs must be absolute http(s) with a host; an alias is `[A-Za-z0-9_-]{3,20}`; a generated code is `Base62.encode(ids.next())`; claiming a code is ONE atomic `putIfAbsent`; a generated code that collides with an existing alias is skipped by taking the next id; `resolve` honours expiry (expired exactly at `expiresAt`) and counts a click only when it returns a url; validation happens before an id is consumed.

Say out loud: why check-then-put is a race and `putIfAbsent` is not; why the clock is injected; counter vs random vs hash codes (predictability, collisions, coordination); `AtomicLong` vs `LongAdder` for hot counters; how clicks would be counted at scale (async, batched) and what that does to `clicks()` accuracy.
