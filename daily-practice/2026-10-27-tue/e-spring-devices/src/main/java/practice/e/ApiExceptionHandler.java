package practice.e;

// TODO: a global handler that turns every failure into a ProblemDetail (application/problem+json).
//   Extend ResponseEntityExceptionHandler so Spring's own exceptions (malformed JSON, type mismatch, method validation...) already
//   come out as problem details, then:
//   1. override handleMethodArgumentNotValid: status 400, title "Invalid request", detail "Validation failed", and a property
//      "errors": a sorted map field -> first message (one entry per field).
//   2. DeviceNotFoundException -> 404.
//   3. DeviceConflictException AND DataIntegrityViolationException (the lost registration race) -> 409.
//   What does the base class add that a hand-written @ExceptionHandler(Exception.class) would get wrong?
public class ApiExceptionHandler {
}
