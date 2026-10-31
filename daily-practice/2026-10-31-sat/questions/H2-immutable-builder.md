# H2. OOP: immutable value object with a Builder (20 min) - `h2/Request.java`

Implement `Request` (final, immutable) and `Request.Builder`.
- `builder(method, url)`: method is upper-cased and must be one of GET, POST, PUT, DELETE; url must start with `/`. Both are checked in `build()` and throw `IllegalStateException`. `GET` and `DELETE` with a non-empty body also fail `build()`.
- `header(name, value)`: name not blank, value not null (else `IllegalArgumentException`); names are case-insensitive, the last value wins. `query(key, value)`: key not empty, value not null; insertion order kept. `body(byte[])`: `null` means no body.
- Immutability: a Builder used after `build()` must not change an existing Request; `body()` returns a copy; `headers()` and `query()` are unmodifiable; the constructor copies the input array.
- `fullUrl()`: `url?k=v&k2=v2` with UTF-8 URL-encoding (`URLEncoder`), or just `url`.
- `toBuilder()` returns an independent pre-filled builder.
- `equals`/`hashCode` consistent for requests that differ only in header-name case; `toString` e.g. `GET /a?x=1 headers=2 body=0B`.
Paper: which `equals`/`hashCode` contract trap hides in a case-insensitive `TreeMap`? Records vs this approach for arrays?
