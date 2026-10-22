package practice.d20261013.h2;

/** GIVEN - the abstraction the high-level policy depends on. */
public interface OrderRepository {
    void save(Order o);
}
