package practice.e;

/** Interface-based projection: Spring Data builds a proxy over the selected columns; the aliases in the query must match the getters. */
public interface CategoryCount {
    String getCategory();

    long getTotal();
}
