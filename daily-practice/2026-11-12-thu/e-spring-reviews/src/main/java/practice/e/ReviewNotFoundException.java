package practice.e;

// TODO(E7a): make Spring answer 404 when this escapes a controller (@ResponseStatus on the exception class)
public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(long id) { super("review " + id + " not found"); }
}
