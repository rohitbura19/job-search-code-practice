package practice.e;

/**
 * TODO: @RestControllerAdvice extending ResponseEntityExceptionHandler (so every Spring MVC error becomes application/problem+json), plus:
 *   ArticleNotFoundException -> 404, DuplicateUrlException -> 409, TooManyTagsException -> 400; each as a ProblemDetail whose detail is the message.
 */
public class ApiExceptionHandler {
}
