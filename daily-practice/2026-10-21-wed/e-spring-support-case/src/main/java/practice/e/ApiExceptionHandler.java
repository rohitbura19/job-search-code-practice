package practice.e;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * TODO - RFC 9457 ProblemDetail responses (application/problem+json):
 *  - override handleMethodArgumentNotValid: title "Validation failed" + property "errors": [{field, message}, ...]  -> 400
 *  - CaseNotFoundException -> 404, title "Case not found"
 *  - StaleVersionException -> 409, title "Stale version"
 *  - InvalidTransitionException -> 409, title "Invalid status change"
 *  - PreconditionRequiredException -> 428, title "If-Match required"
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    // TODO
}
