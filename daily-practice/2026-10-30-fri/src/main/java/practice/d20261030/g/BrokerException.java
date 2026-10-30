package practice.d20261030.g;

/** Given. Unchecked; thrown for every contract violation listed in MessageBroker. */
public class BrokerException extends RuntimeException {
    public BrokerException(String message) { super(message); }
}
