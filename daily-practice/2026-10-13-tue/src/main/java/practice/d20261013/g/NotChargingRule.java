package practice.d20261013.g;

/** Passes when the charger is not in a charging session. */
public class NotChargingRule implements EligibilityRule {
    @Override public boolean test(Device d, Firmware fw) {
        throw new UnsupportedOperationException("TODO");
    }
}
