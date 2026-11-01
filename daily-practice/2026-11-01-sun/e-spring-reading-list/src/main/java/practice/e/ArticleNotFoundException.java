package practice.e;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(long id) { super("article " + id + " not found"); }
}
