package practice.e;

/** Given. Interface-based projection: the getter names must match the aliases of the JPQL query ("as status", "as total"). */
public interface StatusCount {
    Status getStatus();

    long getTotal();
}
