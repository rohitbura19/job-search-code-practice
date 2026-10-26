package practice.e;

import java.util.List;
import java.util.Map;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request body is invalid");
        p.setTitle("Validation failed");
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> Map.of("field", e.getField(), "message", String.valueOf(e.getDefaultMessage()))).toList();
        p.setProperty("errors", errors);
        return handleExceptionInternal(ex, p, headers, status, request);
    }

    // Spring 6.1 trap: as soon as ANY controller parameter carries a constraint (the @Pattern header), method validation takes over and
    // even the @Valid @RequestBody errors arrive here instead of as MethodArgumentNotValidException. Handle both the same way.
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers,
                                                                            HttpStatusCode status, WebRequest request) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request is invalid");
        p.setTitle("Validation failed");
        List<Map<String, String>> errors = new java.util.ArrayList<>();
        ex.getAllValidationResults().forEach(r -> {
            if (r instanceof org.springframework.validation.method.ParameterErrors pe) {
                for (FieldError fe : pe.getFieldErrors()) errors.add(Map.of("field", fe.getField(), "message", String.valueOf(fe.getDefaultMessage())));
            } else {
                String name = String.valueOf(r.getMethodParameter().getParameterName());
                r.getResolvableErrors().forEach(e -> errors.add(Map.of("field", name, "message", String.valueOf(e.getDefaultMessage()))));
            }
        });
        p.setProperty("errors", errors);
        return handleExceptionInternal(ex, p, headers, status, request);
    }

    @ExceptionHandler(RefundException.class)
    ProblemDetail business(RefundException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(ex.status(), ex.getMessage());
        p.setTitle(ex.title());
        return p;
    }

    // The retry-able conflict: the client re-reads and decides again. 409, not 500.
    @ExceptionHandler(OptimisticLockingFailureException.class)
    ProblemDetail concurrent(OptimisticLockingFailureException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "The refund was changed by someone else; reload and retry");
        p.setTitle("Concurrent update");
        return p;
    }
}
