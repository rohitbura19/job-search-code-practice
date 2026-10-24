package practice.e;

// TODO: a @RestControllerAdvice that extends ResponseEntityExceptionHandler and returns ProblemDetail (application/problem+json):
//   - invalid @RequestBody: override handleMethodArgumentNotValid -> 400, title "Validation failed", property "errors" = list of
//     {"field", "message"} maps (one per field error).
//   - ProductNotFoundException -> 404, title "Product not found";  DuplicateSkuException -> 409, title "Duplicate SKU".
//   Parameter-validation errors keep the base class default (400). Why does a plain @ExceptionHandler for MethodArgumentNotValidException
//   not take effect when the advice extends the base class?
public class ApiExceptionHandler {}
