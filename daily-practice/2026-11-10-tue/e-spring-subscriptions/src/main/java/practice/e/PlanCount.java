package practice.e;

/** Given: interface projection for the grouped query (aliases must be `plan` and `total`). */
public interface PlanCount {
    Plan getPlan();
    long getTotal();
}
