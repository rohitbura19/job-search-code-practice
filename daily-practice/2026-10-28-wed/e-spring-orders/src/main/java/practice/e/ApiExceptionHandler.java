package practice.e;

// TODO: @RestControllerAdvice returning application/problem+json (ProblemDetail):
//   OrderNotFoundException -> 404; DuplicateOrderException, IllegalStateTransitionException and
//   DataIntegrityViolationException (the lost race on the unique constraint) -> 409;
//   MethodArgumentNotValidException -> 400 with an "errors" property: map of field path (e.g. lines[0].quantity) -> message.
//   Never leak the exception class or SQL in `detail`.
public class ApiExceptionHandler {
}
