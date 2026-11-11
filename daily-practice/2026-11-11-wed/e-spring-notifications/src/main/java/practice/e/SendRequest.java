package practice.e;

/**
 * TODO(E3): Bean Validation on the components. userId: not null and positive. channel: not null. target: not blank, at most 200 chars.
 *  message: not blank, at most 500 chars.
 */
public record SendRequest(Long userId, Channel channel, String target, String message) {}
