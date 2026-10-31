package practice.d20261031.h2;

import java.util.Map;

/**
 * Immutable HTTP-like request. Implement Request and its Builder (see questions/H2-immutable-builder.md for the rules).
 * Everything except the signatures is yours: fields, private constructor, equals, hashCode, toString.
 */
public final class Request {
    private Request() {}

    public static Builder builder(String method, String url) { throw new UnsupportedOperationException("TODO"); }

    public String method() { throw new UnsupportedOperationException("TODO"); }
    public String url() { throw new UnsupportedOperationException("TODO"); }
    /** unmodifiable; keys compared case-insensitively (later header() with the same name replaces) */
    public Map<String, String> headers() { throw new UnsupportedOperationException("TODO"); }
    /** unmodifiable, insertion order */
    public Map<String, String> query() { throw new UnsupportedOperationException("TODO"); }
    /** a copy on every call (never the internal array); empty array when there is no body */
    public byte[] body() { throw new UnsupportedOperationException("TODO"); }
    /** url + "?" + k=v pairs joined by &, each key and value URL-encoded as UTF-8; just url when there is no query */
    public String fullUrl() { throw new UnsupportedOperationException("TODO"); }
    /** a new Builder pre-filled with this request's values; changing it never affects this request */
    public Builder toBuilder() { throw new UnsupportedOperationException("TODO"); }

    @Override public boolean equals(Object o) { throw new UnsupportedOperationException("TODO"); }
    @Override public int hashCode() { throw new UnsupportedOperationException("TODO"); }
    /** e.g. "GET /a?x=1&y=2 headers=2 body=0B" (method, fullUrl, header count, body length) */
    @Override public String toString() { throw new UnsupportedOperationException("TODO"); }

    public static final class Builder {
        Builder() {}

        public Builder header(String name, String value) { throw new UnsupportedOperationException("TODO"); }
        public Builder query(String key, String value) { throw new UnsupportedOperationException("TODO"); }
        public Builder body(byte[] body) { throw new UnsupportedOperationException("TODO"); }
        public Request build() { throw new UnsupportedOperationException("TODO"); }
    }
}
