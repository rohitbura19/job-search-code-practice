package practice.e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class ShipmentRepositoryTest {
    static final Instant T0 = Instant.parse("2026-11-05T00:00:00Z");
    @Autowired ShipmentRepository repo;
    @Autowired TestEntityManager em;

    Shipment save(String order, ShipmentStatus to) {
        Shipment s = new Shipment(order, "addr " + order, "TRK-" + order, T0);
        if (to != ShipmentStatus.CREATED) s.advanceTo(to, T0);
        return em.persistAndFlush(s);
    }

    @Test void derivedFinders() {
        Shipment s = save("o1", ShipmentStatus.CREATED);
        assertThat(repo.findByOrderId("o1")).get().extracting(Shipment::getId).isEqualTo(s.getId());
        assertThat(repo.findByTrackingId("TRK-o1")).isPresent();
        assertThat(repo.findByOrderId("nope")).isEmpty();
    }

    @Test void orderIdIsUnique() {
        save("dup", ShipmentStatus.CREATED);
        assertThatThrownBy(() -> repo.saveAndFlush(new Shipment("dup", "x", "TRK-other", T0))).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test void statusIsStoredAsString() {
        save("o2", ShipmentStatus.IN_TRANSIT);
        em.clear();
        Object raw = em.getEntityManager().createNativeQuery("select status from shipment where order_id = 'o2'").getSingleResult();
        assertThat(raw).isEqualTo("IN_TRANSIT");
    }

    @Test void searchFiltersOrdersAndCounts() {
        for (int i = 1; i <= 5; i++) save("a" + i, ShipmentStatus.CREATED);
        save("b1", ShipmentStatus.DELIVERED);
        save("b2", ShipmentStatus.DELIVERED);
        var all = repo.search(null, PageRequest.of(0, 4));
        assertThat(all.getTotalElements()).isEqualTo(7);
        assertThat(all.getContent()).extracting(Shipment::getOrderId).containsExactly("a1", "a2", "a3", "a4");
        var delivered = repo.search(ShipmentStatus.DELIVERED, PageRequest.of(0, 10));
        assertThat(delivered.getContent()).extracting(Shipment::getOrderId).containsExactly("b1", "b2");
        assertThat(delivered.getTotalElements()).isEqualTo(2);
        assertThat(repo.search(null, PageRequest.of(1, 4)).getContent()).hasSize(3);
    }

    @Test void versionIncrementsOnUpdate() {
        Shipment s = save("v1", ShipmentStatus.CREATED);
        long v0 = s.getVersion();
        s.advanceTo(ShipmentStatus.PICKED_UP, T0.plusSeconds(1));
        em.flush();
        assertThat(s.getVersion()).isEqualTo(v0 + 1);
    }

    @Test void advanceToIsForwardOnly() {
        Shipment s = new Shipment("x", "y", "TRK-x", T0);
        assertThat(s.advanceTo(ShipmentStatus.IN_TRANSIT, T0.plusSeconds(1))).isTrue();
        assertThat(s.advanceTo(ShipmentStatus.PICKED_UP, T0.plusSeconds(2))).isFalse();
        assertThat(s.advanceTo(ShipmentStatus.IN_TRANSIT, T0.plusSeconds(3))).isFalse();
        assertThat(s.getStatus()).isEqualTo(ShipmentStatus.IN_TRANSIT);
        assertThat(s.getUpdatedAt()).isEqualTo(T0.plusSeconds(1));
    }
}
