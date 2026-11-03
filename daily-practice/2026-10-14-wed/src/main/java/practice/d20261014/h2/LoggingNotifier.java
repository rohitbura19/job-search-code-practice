package practice.d20261014.h2;

import java.util.List;

/**
 * Decorator. Adds "before:<msg>" to the log, calls the inner notifier, then adds "after:<msg>".
 * If the inner call throws, add "failed:<msg>" instead of "after" and rethrow the SAME exception.
 */
public final class LoggingNotifier implements Notifier {
    public LoggingNotifier(Notifier inner, List<String> log) { throw new UnsupportedOperationException("TODO"); }

    @Override public void send(String message) { throw new UnsupportedOperationException("TODO"); }
}
