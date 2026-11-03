package practice.d20261014.h2;

/** Decorator. Counts calls that returned normally (sent) and calls that threw (failed); rethrows the exception. */
public final class MetricNotifier implements Notifier {
    public MetricNotifier(Notifier inner) { throw new UnsupportedOperationException("TODO"); }

    @Override public void send(String message) { throw new UnsupportedOperationException("TODO"); }

    public int sent() { throw new UnsupportedOperationException("TODO"); }

    public int failed() { throw new UnsupportedOperationException("TODO"); }
}
