package practice.e;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(TicketExceptions.NotFound.class)
    ProblemDetail notFound(TicketExceptions.NotFound e) { return problem(HttpStatus.NOT_FOUND, "Ticket not found", e.getMessage()); }

    @ExceptionHandler(TicketExceptions.IllegalTransition.class)
    ProblemDetail illegal(TicketExceptions.IllegalTransition e) { return problem(HttpStatus.CONFLICT, "Illegal status change", e.getMessage()); }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    ProblemDetail stale(ObjectOptimisticLockingFailureException e) {
        return problem(HttpStatus.CONFLICT, "Concurrent update", "The ticket was changed by someone else; reload it and try again.");
    }

    private static ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detail);
        p.setTitle(title);
        return p;
    }
}
