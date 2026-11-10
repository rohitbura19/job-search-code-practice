package practice.e;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

/**
 * TODO(E6): global exception mapping. Extend ResponseEntityExceptionHandler (it already maps validation, malformed JSON, type mismatch and
 *  method-validation errors to 400 ProblemDetail) and add ProblemDetail handlers:
 *  NotFoundException -> 404 (detail = message), ConflictException -> 409 (detail = message),
 *  DataIntegrityViolationException -> 409 with the fixed detail "Duplicate or invalid data" (a lost race on the unique e-mail; never leak SQL text).
 */
public class ApiExceptionHandler {
    public ProblemDetail notFound(NotFoundException e) { throw new UnsupportedOperationException("TODO"); }
    public ProblemDetail conflict(ConflictException e) { throw new UnsupportedOperationException("TODO"); }
    public ProblemDetail integrity(DataIntegrityViolationException e) { throw new UnsupportedOperationException("TODO"); }
}
