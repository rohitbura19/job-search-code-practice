package practice.d20261104.g;

import java.util.Map;

/** Given. Who is asking: userId may be null (anonymous); attrs may be empty (e.g. country, plan). */
public record Context(String userId, Map<String, String> attrs) {
    public Context { attrs = attrs == null ? Map.of() : Map.copyOf(attrs); }

    public static Context of(String userId) { return new Context(userId, Map.of()); }

    public static Context of(String userId, String k, String v) { return new Context(userId, Map.of(k, v)); }
}
