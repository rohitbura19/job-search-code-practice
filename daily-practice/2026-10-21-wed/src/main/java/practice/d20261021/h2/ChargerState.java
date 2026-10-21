package practice.d20261021.h2;

/**
 * H2 - see questions/H2-enum-state-machine.md. Test: ChargerStateTest.
 * Implement each transition INSIDE the constant's own body (constant-specific class bodies).
 * Rules: AVAILABLE --PLUG_IN--> PREPARING; PREPARING --AUTHORIZED--> CHARGING, --PLUG_OUT--> AVAILABLE, --FAULT--> FAULTED;
 * CHARGING --STOP--> FINISHING, --PLUG_OUT--> AVAILABLE, --FAULT--> FAULTED; FINISHING --PLUG_OUT--> AVAILABLE, --FAULT--> FAULTED;
 * FAULTED --RESET--> AVAILABLE. Any other event: stay in the same state. Only CHARGING draws power.
 */
public enum ChargerState {
    AVAILABLE { @Override public ChargerState on(Event e) { throw new UnsupportedOperationException("TODO"); } },
    PREPARING { @Override public ChargerState on(Event e) { throw new UnsupportedOperationException("TODO"); } },
    CHARGING  { @Override public ChargerState on(Event e) { throw new UnsupportedOperationException("TODO"); } },
    FINISHING { @Override public ChargerState on(Event e) { throw new UnsupportedOperationException("TODO"); } },
    FAULTED   { @Override public ChargerState on(Event e) { throw new UnsupportedOperationException("TODO"); } };

    public abstract ChargerState on(Event e);

    public boolean drawsPower() {
        throw new UnsupportedOperationException("TODO");   // default false; override where needed
    }
}
