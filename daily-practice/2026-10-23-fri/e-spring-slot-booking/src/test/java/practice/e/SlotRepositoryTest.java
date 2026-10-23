package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class SlotRepositoryTest {
    static final Instant T0 = Instant.parse("2026-11-01T08:00:00Z");

    @Autowired SlotRepository repo;
    long fullId, openId;

    static Instant h(int hours) { return T0.plusSeconds(3600L * hours); }

    @BeforeEach void seed() {
        Slot open = repo.save(new Slot("st1", h(2), 2));
        Slot full = repo.save(new Slot("st1", h(1), 1));
        repo.save(new Slot("st1", h(3), 3));
        repo.save(new Slot("st1", h(-5), 3));
        repo.save(new Slot("st2", h(2), 3));
        repo.flush();
        openId = open.getId();
        fullId = full.getId();
        assertEquals(1, repo.reserve(fullId));
    }

    @Test void entityGetsGeneratedIdVersionAndDefaults() {
        Slot s = repo.saveAndFlush(new Slot("st9", h(9), 4));
        assertNotNull(s.getId());
        assertNotNull(s.getVersion(), "an @Version field is initialised on persist");
        assertEquals(0, s.getBooked());
    }

    @Test void sameStationAndStartIsRejectedByAUniqueConstraint() {
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(new Slot("st1", h(2), 9)));
    }

    @Test void availableSkipsFullPastAndOtherStationsAndIsEarliestFirst() {
        List<Slot> l = repo.available("st1", h(0));
        assertEquals(List.of(h(2), h(3)), l.stream().map(Slot::getStartsAt).toList());
        assertEquals(List.of(h(-5), h(2), h(3)), repo.available("st1", h(-10)).stream().map(Slot::getStartsAt).toList());
        assertEquals(List.of(), repo.available("nobody", h(-10)));
    }

    @Test void startBoundaryIsInclusive() {
        assertEquals(h(2), repo.available("st1", h(2)).get(0).getStartsAt());
    }

    @Test void reserveCountsUpToCapacityThenReturnsZero() {
        assertEquals(1, repo.reserve(openId));
        assertEquals(1, repo.reserve(openId));
        assertEquals(0, repo.reserve(openId), "full");
        assertEquals(0, repo.reserve(fullId), "was already full");
        assertEquals(0, repo.reserve(123_456), "no such id");
        assertEquals(2, repo.findById(openId).orElseThrow().getBooked(), "the entity is not stale after the bulk update");
    }

    @Test void reservedFullSlotLeavesTheAvailableList() {
        repo.reserve(openId);
        repo.reserve(openId);
        assertEquals(List.of(h(3)), repo.available("st1", h(0)).stream().map(Slot::getStartsAt).toList());
    }
}
