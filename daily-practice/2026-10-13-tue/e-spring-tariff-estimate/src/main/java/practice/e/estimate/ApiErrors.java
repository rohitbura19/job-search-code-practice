package practice.e.estimate;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * TODO - RFC 9457 ProblemDetail (application/problem+json):
 *  - NotFoundException -> 404 with the exception message as detail
 *  - validation errors -> 400: already provided by ResponseEntityExceptionHandler (that is why it is extended)
 */
@RestControllerAdvice
public class ApiErrors extends ResponseEntityExceptionHandler {
    // TODO
}
