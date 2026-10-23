package practice.e;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Extending ResponseEntityExceptionHandler is what lets you customise the MVC's own exceptions: they implement ErrorResponse and the
 * base class owns their handling, so a plain @ExceptionHandler for MethodArgumentNotValidException is not used (the default
 * "Invalid request content." body wins). Override handleMethodArgumentNotValid instead; domain exceptions use @ExceptionHandler.
 */
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

    @ExceptionHandler(SlotNotFoundException.class)
    ProblemDetail notFound(SlotNotFoundException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        p.setTitle("Slot not found");
        return p;
    }

    @ExceptionHandler(SlotFullException.class)
    ProblemDetail full(SlotFullException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        p.setTitle("Slot full");
        return p;
    }
}
