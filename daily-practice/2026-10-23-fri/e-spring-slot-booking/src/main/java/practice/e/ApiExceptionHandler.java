package practice.e;

// TODO: make this a @RestControllerAdvice returning RFC 9457 ProblemDetail (content type application/problem+json):
//   MethodArgumentNotValidException -> 400, title "Validation failed", property "errors" = list of {field, message}
//   SlotNotFoundException           -> 404, title "Slot not found",  detail = the exception message
//   SlotFullException               -> 409, title "Slot full",       detail = the exception message
// Hint: MethodArgumentNotValidException is an ErrorResponse; a plain @ExceptionHandler for it is NOT picked up. What do you extend instead?
// Never return a stack trace or an exception class name to the client.
public class ApiExceptionHandler {
}
