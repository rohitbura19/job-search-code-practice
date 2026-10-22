package practice.d20261013.g;

/** Passes when the firmware lists the device's hardware revision as compatible. */
public class HwRevRule implements EligibilityRule {
    @Override public boolean test(Device d, Firmware fw) {
        throw new UnsupportedOperationException("TODO");
    }
}
