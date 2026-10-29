package practice.e;

public class DuplicateReviewException extends RuntimeException {
    public DuplicateReviewException(String productId, String author) { super(author + " already reviewed " + productId); }
}
