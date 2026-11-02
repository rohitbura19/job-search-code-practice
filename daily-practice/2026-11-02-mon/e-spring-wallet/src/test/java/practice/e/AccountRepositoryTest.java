package practice.e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class AccountRepositoryTest {
    @Autowired AccountRepository accounts;
    @Autowired TransferRepository transfers;
    @Autowired TestEntityManager em;

    @Test void findByOwner() {
        accounts.save(new Account("asha", new BigDecimal("10.00")));
        assertThat(accounts.findByOwner("asha")).isPresent();
        assertThat(accounts.findByOwner("nobody")).isEmpty();
    }

    @Test void findByIdForUpdateReturnsTheRow() {
        Account a = accounts.saveAndFlush(new Account("asha", new BigDecimal("10.00")));
        assertThat(accounts.findByIdForUpdate(a.getId())).hasValueSatisfying(x -> assertThat(x.getOwner()).isEqualTo("asha"));
        assertThat(accounts.findByIdForUpdate(-1L)).isEmpty();
    }

    @Test void ownerIsUnique() {
        accounts.saveAndFlush(new Account("asha", BigDecimal.ONE));
        assertThatThrownBy(() -> accounts.saveAndFlush(new Account("asha", BigDecimal.TEN)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test void versionIncrementsOnUpdate() {
        Account a = accounts.saveAndFlush(new Account("asha", BigDecimal.ONE));
        long v0 = a.getVersion();
        a.setBalance(new BigDecimal("2.00"));
        accounts.saveAndFlush(a);
        em.clear();
        assertThat(accounts.findById(a.getId()).orElseThrow().getVersion()).isEqualTo(v0 + 1);
    }

    @Test void transferLookupByKeyAndKeyIsUnique() {
        transfers.saveAndFlush(new Transfer("k-1", 1L, 2L, new BigDecimal("5.00"), Instant.EPOCH));
        assertThat(transfers.findByIdempotencyKey("k-1")).isPresent();
        assertThat(transfers.findByIdempotencyKey("k-2")).isEmpty();
        assertThatThrownBy(() -> transfers.saveAndFlush(new Transfer("k-1", 3L, 4L, BigDecimal.ONE, Instant.EPOCH)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
