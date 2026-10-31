package practice.d20261031.g;

public class CacheLoadException extends RuntimeException {
    public CacheLoadException(Object key, Throwable cause) { super("load failed for key " + key, cause); }
}
