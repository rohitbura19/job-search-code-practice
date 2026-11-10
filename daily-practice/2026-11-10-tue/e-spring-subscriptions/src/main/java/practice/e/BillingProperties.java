package practice.e;

/**
 * TODO(E7): bind configuration prefix "billing" (relative binding: period-days).
 *  - periodDays defaults to 30 when the property is absent, and must be between 1 and 366 (invalid values must make the context fail at startup).
 *  The record is registered by @ConfigurationPropertiesScan on the application class.
 */
public record BillingProperties(int periodDays) {}
