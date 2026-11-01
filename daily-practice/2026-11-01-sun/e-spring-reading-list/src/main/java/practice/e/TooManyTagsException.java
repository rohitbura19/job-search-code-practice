package practice.e;

public class TooManyTagsException extends RuntimeException {
    public TooManyTagsException(int max) { super("at most " + max + " tags per article"); }
}
