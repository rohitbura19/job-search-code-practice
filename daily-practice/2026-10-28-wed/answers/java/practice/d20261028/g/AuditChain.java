package practice.d20261028.g;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;

/**
 * verify walks the list once, O(n) hashes. Detects edits (hash mismatch), re-hashed edits (the next prevHash no longer matches),
 * deletions and reordering (seq gap), and a forged start (seq 1 must chain from GENESIS). It cannot detect truncation of the TAIL:
 * publish the latest hash (anchor) somewhere the writer cannot edit.
 */
public final class AuditChain {
    private AuditChain() {}

    public static final String GENESIS = "0".repeat(64);

    /** GIVEN. SHA-256 (lowercase hex) of the fields joined by '|' ; the '|' is escaped as "\\|" inside values so fields cannot shift. */
    public static String hash(long seq, String actor, String action, String resource, Instant at, String prevHash) {
        String s = seq + "|" + esc(actor) + "|" + esc(action) + "|" + esc(resource) + "|" + at.toEpochMilli() + "|" + prevHash;
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String esc(String v) { return v.replace("\\", "\\\\").replace("|", "\\|"); }

    /**
     * True if the list is an untampered chain segment: every hash equals the recomputed one, every prevHash equals the previous
     * event's hash, seq increases by exactly 1, and if the first event has seq 1 its prevHash is GENESIS.
     * An empty list is valid. (A segment starting later cannot be anchored; that is the caller's job.)
     */
    public static boolean verify(List<AuditEvent> events) {
        AuditEvent prev = null;
        for (AuditEvent e : events) {
            if (prev == null ? (e.seq() == 1 && !GENESIS.equals(e.prevHash())) : (e.seq() != prev.seq() + 1 || !e.prevHash().equals(prev.hash()))) return false;
            if (!e.hash().equals(hash(e.seq(), e.actor(), e.action(), e.resource(), e.at(), e.prevHash()))) return false;
            prev = e;
        }
        return true;
    }
}
