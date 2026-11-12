package practice.e;

public class DuplicateReviewException extends RuntimeException {
    public DuplicateReviewException(long productId, long userId) { super("user " + userId + " already reviewed product " + productId); }
}
