# B1 - Streams on warehouse stock movements (15 min) - `b1/WarehouseStreams.java`

`Movement(sku, warehouse, type "IN"|"OUT", qty, date)` is given (`signedQty()` helper). One pipeline per method, no loops over the input:

1. `stockBySku`: net stock, alphabetical keys. 2. `busiestWarehouse`: per SKU the warehouse with the largest IN+OUT quantity, tie -> smaller name.
3. `negativeStockSkus`. 4. `totals`: IN and OUT totals in ONE pass (`Collectors.teeing`). 5. `skusPerDay`: date -> sorted set of SKUs, dates ascending.
6. `topOutgoing(k)`: `"sku:qty,sku:qty"`, qty desc, ties by SKU.

Tests include 300k movements with a timeout.
