package practice.e;

/** Interface-based projection: property names must match the JPQL aliases (status, total). */
public interface StatusCount {
    TaskStatus getStatus();

    long getTotal();
}
