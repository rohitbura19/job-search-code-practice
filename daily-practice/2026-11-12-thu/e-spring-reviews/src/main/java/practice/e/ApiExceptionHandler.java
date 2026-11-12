package practice.e;

// TODO(E8): @RestControllerAdvice extending ResponseEntityExceptionHandler (framework errors become ProblemDetail).
//  Add an @ExceptionHandler for DuplicateReviewException AND org.springframework.dao.DataIntegrityViolationException (lost insert race)
//  returning a ProblemDetail: status 409, title "Duplicate review", detail "You already reviewed this product".
public class ApiExceptionHandler {
}
