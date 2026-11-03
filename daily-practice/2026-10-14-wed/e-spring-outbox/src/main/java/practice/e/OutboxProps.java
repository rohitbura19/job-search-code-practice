package practice.e;

/** GIVEN. On paper this is a @ConfigurationProperties(prefix = "outbox") @Validated record with @DefaultValue("100"). */
public record OutboxProps(String topic, int batchSize) {}
