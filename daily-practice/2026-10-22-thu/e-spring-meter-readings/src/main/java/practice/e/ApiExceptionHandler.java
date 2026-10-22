package practice.e;

// TODO: a @RestControllerAdvice that extends the base class which already maps the standard Spring MVC exceptions to ProblemDetail.
// TODO: override the handler for an invalid @RequestBody: status 400, title "Validation failed",
//       property "errors" = [{"field": ..., "message": ...}] for every field error. Content type must be application/problem+json.
public class ApiExceptionHandler {
}
