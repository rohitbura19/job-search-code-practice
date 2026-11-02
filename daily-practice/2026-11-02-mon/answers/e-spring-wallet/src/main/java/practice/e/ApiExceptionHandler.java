package practice.e;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Extends ResponseEntityExceptionHandler so validation and binding errors already become 400 ProblemDetail. */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(WalletExceptions.AccountNotFound.class)
    ProblemDetail notFound(RuntimeException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage()); }

    @ExceptionHandler({WalletExceptions.InsufficientFunds.class, WalletExceptions.DuplicateOwner.class,
            WalletExceptions.IdempotencyConflict.class})
    ProblemDetail conflict(RuntimeException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage()); }

    /** A unique index lost a race (same owner or same Idempotency-Key submitted concurrently). */
    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail integrity(DataIntegrityViolationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "conflicting concurrent request, retry");
    }

    @ExceptionHandler(WalletExceptions.InvalidTransfer.class)
    ProblemDetail unprocessable(RuntimeException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()); }
}
