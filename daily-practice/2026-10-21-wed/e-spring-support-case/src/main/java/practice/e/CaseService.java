package practice.e;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * GIVEN - the controller tests MOCK this class, so you do not implement it here.
 * (The JPA + @Version + Kafka-after-commit version is in answers/E-support-case-api.md - write it on paper.)
 */
@Service
public class CaseService {
    public CaseView open(CreateCaseRequest request) { throw new UnsupportedOperationException("mocked in tests"); }
    /** Throws CaseNotFoundException, StaleVersionException or InvalidTransitionException. */
    public CaseView update(UUID id, long expectedVersion, UpdateCaseRequest request) { throw new UnsupportedOperationException("mocked in tests"); }
    public Page<CaseView> list(CaseStatus status, Pageable pageable) { throw new UnsupportedOperationException("mocked in tests"); }
}
