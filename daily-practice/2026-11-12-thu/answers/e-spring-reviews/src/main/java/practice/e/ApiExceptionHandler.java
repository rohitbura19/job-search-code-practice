package practice.e;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Extends ResponseEntityExceptionHandler so framework errors (bad body, missing header, type mismatch) are RFC 9457 ProblemDetail too. */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    /** The service check can lose a race with a concurrent insert; the unique constraint is the real guard, so both end up as 409. */
    @ExceptionHandler({DuplicateReviewException.class, DataIntegrityViolationException.class})
    ProblemDetail conflict(RuntimeException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "You already reviewed this product");
        pd.setTitle("Duplicate review");
        return pd;
    }
}
