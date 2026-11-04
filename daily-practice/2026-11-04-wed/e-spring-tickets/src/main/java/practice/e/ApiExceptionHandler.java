package practice.e;

// TODO: @RestControllerAdvice returning RFC 9457 ProblemDetail (content type application/problem+json):
//  TicketExceptions.NotFound                                  -> 404, title "Ticket not found",     detail = exception message
//  TicketExceptions.IllegalTransition                         -> 409, title "Illegal status change", detail = exception message
//  org.springframework.orm.ObjectOptimisticLockingFailureException (thrown at flush when @Version differs)
//                                                             -> 409, title "Concurrent update",    detail "The ticket was changed by someone else; reload it and try again."
//  Bean Validation and type-mismatch errors keep the framework's default 400.
public class ApiExceptionHandler {
}
