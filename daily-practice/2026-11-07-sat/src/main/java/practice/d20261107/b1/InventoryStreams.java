package practice.d20261107.b1;

import java.util.List;
import java.util.Map;

/** Streams only: no loops, no mutable accumulators outside collectors. */
public final class InventoryStreams {
    private InventoryStreams() {}

    /** Total stock per category, keys in ascending order (TreeMap). */
    public static Map<String, Long> stockByCategory(List<Product> ps) { throw new UnsupportedOperationException("TODO"); }

    /** Name of the priciest product per category; ties go to the alphabetically smaller name. Keys ascending. */
    public static Map<String, String> priciestPerCategory(List<Product> ps) { throw new UnsupportedOperationException("TODO"); }

    /** The n most frequent tags over all products (a tag repeated inside one product counts each time); ties by tag ascending. */
    public static List<String> topTags(List<Product> ps, int n) { throw new UnsupportedOperationException("TODO"); }

    /** Key true: names with stock < threshold; key false: the rest. Both keys always present, names ascending. */
    public static Map<Boolean, List<String>> splitByLowStock(List<Product> ps, int threshold) { throw new UnsupportedOperationException("TODO"); }

    /** Average price in cents per category (double), keys ascending, over products that are in stock (stock > 0). A category with none is absent. */
    public static Map<String, Double> averagePriceInStock(List<Product> ps) { throw new UnsupportedOperationException("TODO"); }
}
