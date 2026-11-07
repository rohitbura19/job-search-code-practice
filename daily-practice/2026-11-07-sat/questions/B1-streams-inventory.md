# B1 - Streams / collectors (15 min) - `b1/InventoryStreams.java`

`record Product(String name, String category, int stock, long priceCents, List<String> tags)`. Use streams only (no loops).

1. `stockByCategory` -> `Map<String, Long>` total stock per category, keys ascending.
2. `priciestPerCategory` -> `Map<String, String>` name of the priciest product per category; tie -> alphabetically smaller name; keys ascending.
3. `topTags(ps, n)` -> the n most frequent tags over all products (a tag repeated within one product counts every time); ties by tag ascending.
4. `splitByLowStock(ps, threshold)` -> `Map<Boolean, List<String>>`: `true` = names with `stock < threshold`, `false` = the rest; both keys always present; names ascending.
5. `averagePriceInStock` -> `Map<String, Double>` average `priceCents` per category over products with `stock > 0`; keys ascending; categories with no such product are absent.
