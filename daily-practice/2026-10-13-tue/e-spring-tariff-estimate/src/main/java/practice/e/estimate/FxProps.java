package practice.e.estimate;

import java.time.Duration;

/**
 * TODO - immutable binding of "fx.base-url" and "fx.timeout" (a record bound through its constructor), validated at startup:
 * baseUrl not blank, timeout not null. Add @ConfigurationProperties(prefix = "fx") and @Validated plus the constraints.
 */
public record FxProps(String baseUrl, Duration timeout) {}
