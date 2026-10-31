package practice.e;

/**
 * TODO: global handler producing RFC 9457 problem details (application/problem+json). Extend the Spring base class that already maps
 * validation and malformed-body errors to 400.
 *   NoteNotFoundException -> 404 "Note not found";  QuotaExceededException -> 409 "Note quota exceeded";
 *   StaleVersionException -> 409 "Stale version";   DataIntegrityViolationException -> 409 "Duplicate title".
 * The problem detail's "detail" is the exception message (for the integrity violation use a fixed text, never the SQL message).
 */
public class ApiExceptionHandler {}
