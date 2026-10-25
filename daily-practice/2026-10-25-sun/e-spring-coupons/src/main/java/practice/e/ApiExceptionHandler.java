package practice.e;

// TODO: a @RestControllerAdvice that extends ResponseEntityExceptionHandler and returns ProblemDetail (application/problem+json):
//   - invalid @RequestBody: override handleMethodArgumentNotValid -> 400, title "Validation failed", property "errors" = list of
//     {"field", "message"} maps (one per field error).
//   - CouponException -> its status() with title() as the title and getMessage() as the detail.
//   - IllegalArgumentException -> 400, title "Invalid request".
public class ApiExceptionHandler {}
