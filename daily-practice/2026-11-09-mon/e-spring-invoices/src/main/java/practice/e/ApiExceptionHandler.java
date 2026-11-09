package practice.e;

import org.springframework.http.ProblemDetail;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

/**
 * TODO(E6): global exception mapping. Extend ResponseEntityExceptionHandler (it already turns bean-validation, malformed-JSON,
 *  type-mismatch and method-validation errors into 400 ProblemDetail) and add handlers returning a ProblemDetail:
 *  NotFoundException -> 404, ConflictException -> 409; the exception message is the detail.
 */
public class ApiExceptionHandler {
    public ProblemDetail notFound(NotFoundException e) { throw new UnsupportedOperationException("TODO"); }
    public ProblemDetail conflict(ConflictException e) { throw new UnsupportedOperationException("TODO"); }
}
