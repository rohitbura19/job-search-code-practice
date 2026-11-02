package practice.e;

/** Given. */
public final class WalletExceptions {
    private WalletExceptions() {}

    public static class AccountNotFound extends RuntimeException {
        public AccountNotFound(Long id) { super("account " + id + " not found"); }
    }

    public static class InsufficientFunds extends RuntimeException {
        public InsufficientFunds(String msg) { super(msg); }
    }

    public static class InvalidTransfer extends RuntimeException {
        public InvalidTransfer(String msg) { super(msg); }
    }

    public static class DuplicateOwner extends RuntimeException {
        public DuplicateOwner(String owner) { super("owner already has an account: " + owner); }
    }

    public static class IdempotencyConflict extends RuntimeException {
        public IdempotencyConflict(String key) { super("Idempotency-Key " + key + " was used with a different request"); }
    }
}
