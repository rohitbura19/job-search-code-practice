package practice.e;

import java.time.Clock;
import java.util.Objects;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefundService {
    private final RefundRepository repo;
    private final RefundProperties props;
    private final Clock clock;

    public RefundService(RefundRepository repo, RefundProperties props, Clock clock) {
        this.repo = repo;
        this.props = props;
        this.clock = clock;
    }

    // Deliberately NOT @Transactional: after a constraint violation inside one transaction the persistence context is unusable
    // (the transaction is rollback-only), so the re-read below would fail. Each repository call is its own short transaction.
    public CreateResult create(String key, CreateRefundRequest r) {
        long amount = r.amountPaise();
        if (amount > props.maxRefundPaise()) throw RefundException.tooLarge(amount, props.maxRefundPaise());
        var existing = repo.findByIdempotencyKey(key);
        if (existing.isPresent()) return replayOrConflict(key, existing.get(), r);
        RefundStatus status = amount <= props.autoApproveUpToPaise() ? RefundStatus.APPROVED : RefundStatus.PENDING;
        try {
            return new CreateResult(RefundView.of(repo.saveAndFlush(new Refund(r.paymentId(), amount, r.reason(), key, status, clock.instant()))), false);
        } catch (DataIntegrityViolationException e) {
            return replayOrConflict(key, repo.findByIdempotencyKey(key).orElseThrow(() -> e), r);
        }
    }

    private static CreateResult replayOrConflict(String key, Refund old, CreateRefundRequest r) {
        if (old.getAmountPaise() == r.amountPaise() && Objects.equals(old.getPaymentId(), r.paymentId())) return new CreateResult(RefundView.of(old), true);
        throw RefundException.keyReused(key);
    }

    @Transactional
    public RefundView decide(long id, boolean approved) {
        Refund r = repo.findById(id).orElseThrow(() -> RefundException.notFound(id));
        if (r.getStatus() != RefundStatus.PENDING) throw RefundException.alreadyDecided(id, r.getStatus());
        r.decide(approved);                       // dirty checking flushes UPDATE ... WHERE id = ? AND version = ? at commit
        return RefundView.of(r);
    }

    @Transactional(readOnly = true)
    public RefundView get(long id) {
        return RefundView.of(repo.findById(id).orElseThrow(() -> RefundException.notFound(id)));
    }

    @Transactional(readOnly = true)
    public PageView<RefundView> list(String paymentId, Pageable pageable) {
        var page = paymentId == null ? repo.findAll(pageable) : repo.findByPaymentId(paymentId, pageable);
        return PageView.of(page, RefundView::of);
    }
}
