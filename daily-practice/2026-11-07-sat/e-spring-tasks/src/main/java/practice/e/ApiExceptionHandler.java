package practice.e;

/**
 * TODO(E6): turn this into a global REST advice that extends ResponseEntityExceptionHandler (so framework exceptions such as body
 * validation, parameter validation and type mismatch become application/problem+json 400s) and add handlers returning ProblemDetail:
 * NotFoundException -> 404, ConflictException -> 409, IllegalArgumentException -> 400; detail = the exception message.
 */
public class ApiExceptionHandler {}
