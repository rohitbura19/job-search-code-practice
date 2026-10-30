package practice.e;

/**
 * TODO: global handler producing RFC 9457 problem details (application/problem+json).
 *   StockNotFoundException -> 404, title "Stock item not found";  OutOfStockException -> 409, title "Not enough stock".
 *   Bean-validation and malformed-body errors must come out as 400 problem+json too: extend the Spring base class that already maps them.
 */
public class ApiExceptionHandler {}
