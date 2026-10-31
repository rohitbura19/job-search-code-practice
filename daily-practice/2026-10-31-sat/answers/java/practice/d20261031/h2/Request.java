package practice.d20261031.h2;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * Immutability recipe: final class, private final fields, defensive copies on the way in (Builder.build) AND on the way out (body()),
 * unmodifiable views for maps, no setters. The Builder is the mutable twin; build() snapshots it, so later builder calls cannot leak
 * into an existing Request. Validation lives in build() (the one place an invalid object could be created), reporting every rule.
 * Headers: TreeMap(CASE_INSENSITIVE_ORDER) gives case-insensitive lookup and equality, but its order is alphabetical, not insertion;
 * the spec asks for case-insensitive lookup only. equals/hashCode use Arrays.equals/hashCode for the array.
 * Trap: TreeMap(CASE_INSENSITIVE_ORDER).equals() ignores key case but its hashCode() does NOT, so equal requests would hash differently
 * (breaks the equals/hashCode contract). hashCode therefore hashes lower-cased keys, summed so the order does not matter.
 */
public final class Request {
    private static final Set<String> METHODS = Set.of("GET", "POST", "PUT", "DELETE");
    private static final Set<String> NO_BODY = Set.of("GET", "DELETE");

    private final String method, url;
    private final Map<String, String> headers, query;
    private final byte[] body;

    private Request(Builder b) {
        method = b.method;
        url = b.url;
        Map<String, String> h = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        h.putAll(b.headers);
        headers = Collections.unmodifiableMap(h);
        query = Collections.unmodifiableMap(new LinkedHashMap<>(b.query));
        body = b.body.clone();
    }

    public static Builder builder(String method, String url) { return new Builder(method, url); }

    public String method() { return method; }
    public String url() { return url; }
    public Map<String, String> headers() { return headers; }
    public Map<String, String> query() { return query; }
    public byte[] body() { return body.clone(); }

    public String fullUrl() {
        if (query.isEmpty()) return url;
        StringBuilder sb = new StringBuilder(url).append('?');
        boolean first = true;
        for (Map.Entry<String, String> e : query.entrySet()) {
            if (!first) sb.append('&');
            first = false;
            sb.append(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)).append('=').append(URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8));
        }
        return sb.toString();
    }

    public Builder toBuilder() {
        Builder b = new Builder(method, url);
        b.headers.putAll(headers);
        b.query.putAll(query);
        b.body = body.clone();
        return b;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request r)) return false;
        return method.equals(r.method) && url.equals(r.url) && headers.equals(r.headers) && query.equals(r.query) && Arrays.equals(body, r.body);
    }

    @Override public int hashCode() { 
        int h = 0;
        for (Map.Entry<String, String> e : headers.entrySet()) h += e.getKey().toLowerCase(java.util.Locale.ROOT).hashCode() ^ e.getValue().hashCode();
        return Objects.hash(method, url, h, query, Arrays.hashCode(body));
    }

    @Override public String toString() { return method + " " + fullUrl() + " headers=" + headers.size() + " body=" + body.length + "B"; }

    public static final class Builder {
        private final String method, url;
        private final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        private final Map<String, String> query = new LinkedHashMap<>();
        private byte[] body = new byte[0];

        Builder(String method, String url) {
            this.method = method == null ? null : method.toUpperCase(java.util.Locale.ROOT);
            this.url = url;
        }

        public Builder header(String name, String value) {
            if (name == null || name.isBlank() || value == null) throw new IllegalArgumentException("bad header");
            headers.put(name.trim(), value);
            return this;
        }

        public Builder query(String key, String value) {
            if (key == null || key.isEmpty() || value == null) throw new IllegalArgumentException("bad query");
            query.put(key, value);
            return this;
        }

        public Builder body(byte[] b) { body = b == null ? new byte[0] : b.clone(); return this; }

        public Request build() {
            if (method == null || !METHODS.contains(method)) throw new IllegalStateException("unsupported method: " + method);
            if (url == null || !url.startsWith("/")) throw new IllegalStateException("url must start with /");
            if (NO_BODY.contains(method) && body.length > 0) throw new IllegalStateException(method + " cannot have a body");
            return new Request(this);
        }
    }
}
