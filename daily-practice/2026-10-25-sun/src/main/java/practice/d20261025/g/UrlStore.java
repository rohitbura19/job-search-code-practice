package practice.d20261025.g;

import java.util.Optional;

/** Thread-safe storage of short urls and their click counters. */
public interface UrlStore {
    /** Atomically store u unless its code is already present. true = stored, false = the code was taken (the stored value is unchanged). */
    boolean putIfAbsent(ShortUrl u);

    Optional<ShortUrl> find(String code);

    /** Atomically add one click; returns the new total. Unknown code -> java.util.NoSuchElementException. */
    long incrementClicks(String code);

    /** Unknown code -> java.util.NoSuchElementException. */
    long clicks(String code);
}
