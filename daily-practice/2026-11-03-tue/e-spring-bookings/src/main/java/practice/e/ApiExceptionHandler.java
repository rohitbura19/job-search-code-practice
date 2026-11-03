package practice.e;

// TODO: @RestControllerAdvice mapping the BookingExceptions to RFC 9457 ProblemDetail responses
//  (return ProblemDetail from @ExceptionHandler methods; content type becomes application/problem+json):
//  NotFound -> 404, Overlap -> 409, TooLong and InPast -> 422. Set a short title and use the exception message as the detail.
//  Bean Validation failures stay 400 (default handling).
public class ApiExceptionHandler {
}
