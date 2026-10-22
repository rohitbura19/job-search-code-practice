package practice.e;

import java.time.Duration;

/**
 * TODO: bind "commands.topic" and "commands.default-ttl" (ISO-8601 duration such as PT5M), immutable.
 * One annotation is missing. AppConfig already registers this class with @EnableConfigurationProperties.
 */
public record CommandProps(String topic, Duration defaultTtl) {}
