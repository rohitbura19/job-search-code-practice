package practice.d20261013.g;

import java.util.List;

/** Applies a chain of rules. */
public final class Eligibility {
    private Eligibility() {}

    /** The devices (input order kept) for which every rule passes. No rules -> everybody is eligible. */
    public static List<Device> eligible(List<Device> devices, Firmware fw, List<EligibilityRule> rules) {
        throw new UnsupportedOperationException("TODO");
    }
}
