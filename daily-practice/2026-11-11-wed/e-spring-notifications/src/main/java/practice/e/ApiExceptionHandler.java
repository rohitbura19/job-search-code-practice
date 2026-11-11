package practice.e;

import org.springframework.http.ProblemDetail;
import practice.e.Exceptions.InvalidTargetException;
import practice.e.Exceptions.NotFoundException;

/**
 * TODO(E6): global exception mapping (a @RestControllerAdvice). Extend ResponseEntityExceptionHandler (it already maps validation, malformed JSON, missing header,
 *  type mismatch and method-validation errors to 400 ProblemDetail) and add ProblemDetail handlers:
 *  NotFoundException -> 404 (detail = message); InvalidTargetException -> 422 (detail = message, title "Invalid target").
 */
public class ApiExceptionHandler {
    public ProblemDetail notFound(NotFoundException e) { throw new UnsupportedOperationException("TODO"); }
    public ProblemDetail invalidTarget(InvalidTargetException e) { throw new UnsupportedOperationException("TODO"); }
}
