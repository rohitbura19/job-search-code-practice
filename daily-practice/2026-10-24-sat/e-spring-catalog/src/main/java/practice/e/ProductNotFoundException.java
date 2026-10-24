package practice.e;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(long id) { super("No product with id " + id); }
}
