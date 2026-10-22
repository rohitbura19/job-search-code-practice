package practice.e;

/**
 * TODO: add Bean Validation. type is required. ttlSeconds is optional (null = use the default TTL) but when present
 * it must be 1..3600. Which annotations? (jakarta.validation.constraints)
 */
public record CommandRequest(CommandType type, Integer ttlSeconds) {}
