package practice.d20261021.h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static practice.d20261021.h2.ChargerState.*;
import static practice.d20261021.h2.Event.*;

class ChargerStateTest {

    @Test @DisplayName("the event sequence from the question")
    void sequence() {
        ChargerState s = AVAILABLE; StringBuilder sb = new StringBuilder();
        for (Event e : new Event[]{PLUG_IN, AUTHORIZED, STOP, FAULT, PLUG_OUT, RESET, PLUG_OUT}) { s = s.on(e); sb.append(s).append(' '); }
        assertEquals("PREPARING CHARGING FINISHING FAULTED FAULTED AVAILABLE AVAILABLE", sb.toString().trim());
    }

    @Test @DisplayName("every (state, event) pair: the full transition table")
    void fullTable() {
        Map<ChargerState, Map<Event, ChargerState>> moves = new EnumMap<>(ChargerState.class);
        moves.put(AVAILABLE, Map.of(PLUG_IN, PREPARING));
        moves.put(PREPARING, Map.of(AUTHORIZED, CHARGING, PLUG_OUT, AVAILABLE, FAULT, FAULTED));
        moves.put(CHARGING, Map.of(STOP, FINISHING, PLUG_OUT, AVAILABLE, FAULT, FAULTED));
        moves.put(FINISHING, Map.of(PLUG_OUT, AVAILABLE, FAULT, FAULTED));
        moves.put(FAULTED, Map.of(RESET, AVAILABLE));
        for (ChargerState st : ChargerState.values())
            for (Event e : Event.values())
                assertEquals(moves.get(st).getOrDefault(e, st), st.on(e), st + " + " + e);   // anything not listed: stay
    }

    @Test @DisplayName("only CHARGING draws power")
    void drawsPower() {
        for (ChargerState st : ChargerState.values()) assertEquals(st == CHARGING, st.drawsPower(), st.name());
    }

    @Test @DisplayName("transitions use constant-specific bodies (each constant overrides on)")
    void constantBodies() {
        for (ChargerState st : ChargerState.values()) {
            assertNotSame(ChargerState.class, st.getClass());
            assertSame(ChargerState.class, st.getDeclaringClass());
        }
    }

    @Test @DisplayName("Code.of looks up by the short code, case-sensitive, and rejects unknown codes")
    void codeLookup() {
        assertSame(Code.ONLINE, Code.of("O"));
        assertSame(Code.OFFLINE, Code.of("F"));
        assertThrows(IllegalArgumentException.class, () -> Code.of("o"));
        assertThrows(IllegalArgumentException.class, () -> Code.of("X"));
        assertThrows(IllegalArgumentException.class, () -> Code.of(null));
    }
}
