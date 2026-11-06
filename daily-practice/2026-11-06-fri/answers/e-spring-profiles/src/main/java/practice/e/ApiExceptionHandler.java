package practice.e;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Extends ResponseEntityExceptionHandler so bean-validation failures (400) already come back as ProblemDetail. */
@RestControllerAdvice
class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exceptions.ProfileNotFoundException.class)
    ProblemDetail notFound(RuntimeException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage()); }

    @ExceptionHandler(Exceptions.DuplicateEmailException.class)
    ProblemDetail conflict(RuntimeException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage()); }

    @ExceptionHandler({Exceptions.StaleVersionException.class, OptimisticLockingFailureException.class})
    ProblemDetail stale(RuntimeException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.PRECONDITION_FAILED, e.getMessage()); }

    @ExceptionHandler(Exceptions.PreconditionRequiredException.class)
    ProblemDetail required(RuntimeException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.PRECONDITION_REQUIRED, e.getMessage()); }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail badRequest(RuntimeException e) { return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage()); }
}
