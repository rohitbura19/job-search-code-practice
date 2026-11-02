package practice.e;

import java.time.Clock;

// TODO: make this a Spring service; the methods that write must run in a transaction, the read one read-only.
public class WalletService {
    public WalletService(AccountRepository accounts, TransferRepository transfers, WalletProps props, Clock clock) {
        throw new UnsupportedOperationException("TODO");
    }

    public AccountView createAccount(AccountRequest req) {
        // TODO: DuplicateOwner if the owner exists; opening balance defaults to 0.
        throw new UnsupportedOperationException("TODO");
    }

    public AccountView getAccount(Long id) {
        // TODO: AccountNotFound if missing.
        throw new UnsupportedOperationException("TODO");
    }

    public TransferOutcome transfer(String key, TransferRequest req) {
        // TODO, in this order:
        // 1. Replay: same key + same payload (compare amount with compareTo) -> stored transfer, created=false; same key + different payload -> IdempotencyConflict.
        // 2. fromId == toId, or amount > props.maxTransfer() -> InvalidTransfer (before any lock).
        // 3. Lock both accounts with findByIdForUpdate in ASCENDING id order whatever the direction (avoids deadlock); missing -> AccountNotFound.
        // 4. balance < amount -> InsufficientFunds; else move the money and save a Transfer stamped with clock.instant().
        throw new UnsupportedOperationException("TODO");
    }
}
