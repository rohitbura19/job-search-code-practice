# G - LLD of F1: in-memory `UrlShortenerService` (sketch 8 min, code 22 min)

Implement `g/Base62` and `g/UrlShortenerService` (given: `IdGenerator`, `ShortLink`, `AliasTakenException`). The class Javadoc of the service is the full spec: validation, custom aliases, generated codes that skip taken ones, TTL via the injected `Clock`, exact click counting under concurrency, purge.

Must hold: exactly one winner when 8 threads claim the same alias; an expired alias can be reused; rejected requests consume no ids. Sketch first in `my-designs/G-classes-sketch.md`. Test: `UrlShortenerTest`.
