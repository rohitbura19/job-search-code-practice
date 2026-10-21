package practice.d20261021.h2;

/** H2 - lookup by a short code. Hint: the constructor cannot touch a static field (why?). */
public enum Code {
    ONLINE("O"), OFFLINE("F");

    final String code;

    Code(String code) { this.code = code; }

    /** "O" -> ONLINE, "F" -> OFFLINE; anything else (including null, or lower case) -> IllegalArgumentException. */
    public static Code of(String code) {
        throw new UnsupportedOperationException("TODO");
    }
}
