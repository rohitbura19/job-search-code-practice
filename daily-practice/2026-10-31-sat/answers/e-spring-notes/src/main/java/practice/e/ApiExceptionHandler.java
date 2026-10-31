package practice.e;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private static ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detail);
        p.setTitle(title);
        return p;
    }

    @ExceptionHandler(NoteNotFoundException.class)
    ProblemDetail notFound(NoteNotFoundException e) { return problem(HttpStatus.NOT_FOUND, "Note not found", e.getMessage()); }

    @ExceptionHandler(QuotaExceededException.class)
    ProblemDetail quota(QuotaExceededException e) { return problem(HttpStatus.CONFLICT, "Note quota exceeded", e.getMessage()); }

    @ExceptionHandler(StaleVersionException.class)
    ProblemDetail stale(StaleVersionException e) { return problem(HttpStatus.CONFLICT, "Stale version", e.getMessage()); }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail duplicate(DataIntegrityViolationException e) { return problem(HttpStatus.CONFLICT, "Duplicate title", "this owner already has a note with that title"); }
}
