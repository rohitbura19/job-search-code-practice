package practice.e;

// TODO: a @RestControllerAdvice that extends ResponseEntityExceptionHandler and returns ProblemDetail (application/problem+json):
//   - invalid @RequestBody: override handleMethodArgumentNotValid -> 400, title "Validation failed", property "errors" = list of
//     {"field", "message"} maps (one per field error).
//   - ALSO override handleHandlerMethodValidationException with the same shape (field = body field, or the parameter name for the
//     header). Why both? In Spring 6.1, once any controller parameter has a constraint (the @Pattern on the header), @Valid body errors
//     are reported through this second exception instead.
//   - RefundException -> its status() with title() as the title and getMessage() as the detail.
//   - ObjectOptimisticLockingFailureException (two people decided the same refund) -> 409, title "Concurrent update".
public class ApiExceptionHandler {}
