package practice.e;

public class QuotaExceededException extends RuntimeException {
    public QuotaExceededException(String owner, int max) { super(owner + " already has " + max + " notes"); }
}
