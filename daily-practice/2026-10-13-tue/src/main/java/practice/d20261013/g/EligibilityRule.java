package practice.d20261013.g;

/** GIVEN. Specification / chain: a device is eligible only if ALL rules pass. */
public interface EligibilityRule {
    boolean test(Device d, Firmware fw);
}
