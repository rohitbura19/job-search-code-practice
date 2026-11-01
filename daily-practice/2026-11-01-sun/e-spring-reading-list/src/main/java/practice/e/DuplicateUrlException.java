package practice.e;

public class DuplicateUrlException extends RuntimeException {
    public DuplicateUrlException(String url) { super("url already saved: " + url); }
}
