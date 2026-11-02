package practice.e;

import java.math.BigDecimal;
import java.time.Clock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
    private final AccountRepository accounts;
    private final TransferRepository transfers;
    private final WalletProps props;
    private final Clock clock;

    public WalletService(AccountRepository accounts, TransferRepository transfers, WalletProps props, Clock clock) {
        this.accounts = accounts; this.transfers = transfers; this.props = props; this.clock = clock;
    }

    @Transactional
    public AccountView createAccount(AccountRequest req) {
        if (accounts.findByOwner(req.owner()).isPresent()) throw new WalletExceptions.DuplicateOwner(req.owner());
        BigDecimal opening = req.openingBalance() == null ? BigDecimal.ZERO : req.openingBalance();
        return AccountView.of(accounts.save(new Account(req.owner(), opening)));
    }

    @Transactional(readOnly = true)
    public AccountView getAccount(Long id) {
        return accounts.findById(id).map(AccountView::of).orElseThrow(() -> new WalletExceptions.AccountNotFound(id));
    }

    /**
     * 1. Replay: same key + same payload -> the stored transfer (created=false); same key + different payload -> IdempotencyConflict.
     * 2. fromId == toId or amount > props.maxTransfer() -> InvalidTransfer.
     * 3. Lock both accounts with findByIdForUpdate in ASCENDING id order (two opposite transfers must not deadlock);
     *    a missing account -> AccountNotFound.
     * 4. balance < amount -> InsufficientFunds; else move the money and save a Transfer stamped with clock.instant().
     */
    @Transactional
    public TransferOutcome transfer(String key, TransferRequest req) {
        var existing = transfers.findByIdempotencyKey(key);
        if (existing.isPresent()) {
            Transfer t = existing.get();
            boolean same = t.getFromId().equals(req.fromId()) && t.getToId().equals(req.toId()) && t.getAmount().compareTo(req.amount()) == 0;
            if (!same) throw new WalletExceptions.IdempotencyConflict(key);
            return new TransferOutcome(TransferView.of(t), false);
        }
        if (req.fromId().equals(req.toId())) throw new WalletExceptions.InvalidTransfer("cannot transfer to the same account");
        if (req.amount().compareTo(props.maxTransfer()) > 0) throw new WalletExceptions.InvalidTransfer("amount exceeds " + props.maxTransfer());

        Long low = Math.min(req.fromId(), req.toId()), high = Math.max(req.fromId(), req.toId());
        Account first = lock(low), second = lock(high);
        Account from = req.fromId().equals(low) ? first : second;
        Account to = from == first ? second : first;

        if (from.getBalance().compareTo(req.amount()) < 0) throw new WalletExceptions.InsufficientFunds("balance " + from.getBalance() + " < " + req.amount());
        from.setBalance(from.getBalance().subtract(req.amount()));
        to.setBalance(to.getBalance().add(req.amount()));
        Transfer saved = transfers.save(new Transfer(key, req.fromId(), req.toId(), req.amount(), clock.instant()));
        return new TransferOutcome(TransferView.of(saved), true);
    }

    private Account lock(Long id) {
        return accounts.findByIdForUpdate(id).orElseThrow(() -> new WalletExceptions.AccountNotFound(id));
    }
}
