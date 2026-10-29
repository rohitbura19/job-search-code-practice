package practice.e;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(long id) { super("review " + id + " not found"); }
}
