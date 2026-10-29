package practice.e;

// TODO: @RestControllerAdvice returning application/problem+json (ProblemDetail):
//   ReviewNotFoundException -> 404; DuplicateReviewException and DataIntegrityViolationException (the lost race on the unique
//   constraint) -> 409 without leaking SQL; MethodArgumentNotValidException -> 400 with an "errors" property: map of field -> message.
public class ApiExceptionHandler {
}
