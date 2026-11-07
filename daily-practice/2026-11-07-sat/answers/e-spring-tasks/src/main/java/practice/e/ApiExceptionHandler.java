package practice.e;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Extending ResponseEntityExceptionHandler gives RFC 9457 ProblemDetail bodies (application/problem+json) for the framework's own
 * exceptions: body validation (400), bean validation on parameters (HandlerMethodValidationException, 400), type mismatch (400), ...
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exceptions.NotFoundException.class)
    ProblemDetail notFound(Exceptions.NotFoundException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage()); }

    @ExceptionHandler(Exceptions.ConflictException.class)
    ProblemDetail conflict(Exceptions.ConflictException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage()); }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail badRequest(IllegalArgumentException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage()); }
}
