package practice.e;

import java.time.Clock;
import org.springframework.data.domain.Pageable;

// TODO: make this a Spring service. Decide per method whether it is @Transactional, readOnly, or NOT transactional at all (and say why).
public class RefundService {
    private final RefundRepository repo;
    private final RefundProperties props;
    private final Clock clock;

    public RefundService(RefundRepository repo, RefundProperties props, Clock clock) {
        this.repo = repo;
        this.props = props;
        this.clock = clock;
    }

    /**
     * 1. amount > props.maxRefundPaise() -> RefundException.tooLarge, before touching the repository.
     * 2. A refund with this key exists: same paymentId AND amount -> CreateResult(view, true) (a retry, nothing saved);
     *    different -> RefundException.keyReused.
     * 3. Otherwise save a new refund: APPROVED when amount <= props.autoApproveUpToPaise(), else PENDING; createdAt = clock.instant().
     *    Use saveAndFlush. If two requests with the same key race past step 2, the unique constraint fires
     *    (DataIntegrityViolationException): re-read by key and answer as in step 2 (replay or keyReused). That re-read only works
     *    if create() is NOT one big transaction: why?
     */
    public CreateResult create(String idempotencyKey, CreateRefundRequest request) { throw new UnsupportedOperationException("TODO"); }

    /** Unknown id -> notFound. Not PENDING -> alreadyDecided. Otherwise decide() and return the view (the version check guards races). */
    public RefundView decide(long id, boolean approved) { throw new UnsupportedOperationException("TODO"); }

    public RefundView get(long id) { throw new UnsupportedOperationException("TODO"); }

    /** paymentId null -> all refunds, else only that payment's; mapped to the PageView shape. */
    public PageView<RefundView> list(String paymentId, Pageable pageable) { throw new UnsupportedOperationException("TODO"); }
}
