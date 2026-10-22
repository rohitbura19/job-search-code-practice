package practice.e.estimate;

/** GIVEN. */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String regionCode) { super("No tariff for region " + regionCode); }
}
