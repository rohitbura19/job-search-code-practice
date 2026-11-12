package practice.e;

/** Interface-based projection: Spring Data maps the query aliases `rating` and `total` to these getters. */
public interface RatingCount {
    int getRating();
    long getTotal();
}
