package practice.e;

/** GIVEN. Stands in for the Spring Data repository (mocked in the tests). */
public interface AlertRepository {
    Alert save(Alert alert);
}
