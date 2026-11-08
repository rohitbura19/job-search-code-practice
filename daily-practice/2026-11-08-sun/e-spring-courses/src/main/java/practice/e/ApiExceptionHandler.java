package practice.e;

/**
 * TODO(E7): @RestControllerAdvice extending ResponseEntityExceptionHandler (so bean validation errors become 400 ProblemDetail).
 * Add handlers: NotFoundException -> 404 ProblemDetail (title "Not found", detail = message);
 * ConflictException -> 409 ProblemDetail (title "Conflict", detail = message).
 */
public class ApiExceptionHandler {
}
