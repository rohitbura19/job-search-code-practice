package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest
class ProfileRepositoryTest {
    @Autowired ProfileRepository repo;
    @Autowired TestEntityManager em;

    private Profile save(String email, String name, String city) {
        return repo.saveAndFlush(new Profile(email, name, new Address(city, "IN")));
    }

    @Test void findsByEmailIgnoringCase() {
        save("ann@x.com", "Ann", "Pune");
        assertTrue(repo.findByEmailIgnoreCase("ANN@x.com").isPresent());
        assertTrue(repo.existsByEmailIgnoreCase("Ann@X.com"));
        assertFalse(repo.existsByEmailIgnoreCase("bob@x.com"));
    }

    @Test void emailIsUnique() {
        save("ann@x.com", "Ann", "Pune");
        assertThrows(DataIntegrityViolationException.class, () -> save("ann@x.com", "Other", "Delhi"));
    }

    @Test void embeddedAddressRoundTrips() {
        Long id = save("ann@x.com", "Ann", "Pune").getId();
        em.clear();
        Profile p = repo.findById(id).orElseThrow();
        assertEquals("Pune", p.getAddress().city());
        assertEquals("IN", p.getAddress().country());
    }

    @Test void versionStartsAtZeroAndIncrementsOnUpdate() {
        Profile p = save("ann@x.com", "Ann", "Pune");
        assertEquals(0, p.getVersion());
        p.setDisplayName("Anna");
        repo.saveAndFlush(p);
        assertEquals(1, p.getVersion());
    }

    @Test void staleDetachedCopyIsRejected() {
        Profile p = save("ann@x.com", "Ann", "Pune");
        Long id = p.getId();
        em.clear();
        Profile stale = repo.findById(id).orElseThrow();
        em.detach(stale);
        Profile fresh = repo.findById(id).orElseThrow();
        fresh.setDisplayName("Fresh");
        repo.saveAndFlush(fresh);
        stale.setDisplayName("Stale");
        assertThrows(OptimisticLockingFailureException.class, () -> repo.saveAndFlush(stale));
    }

    @Test void specificationsFilterAndNullDropsOut() {
        save("a@x.com", "Ann Lee", "Pune");
        save("b@x.com", "Bob Lee", "Pune");
        save("c@x.com", "Cara", "Delhi");
        var page = PageRequest.of(0, 10, Sort.by("email"));
        assertEquals(List.of("a@x.com", "b@x.com"), emails(ProfileSpecs.cityIs("PUNE"), page));
        assertEquals(List.of("a@x.com", "b@x.com"), emails(ProfileSpecs.nameContains("lee"), page));
        assertEquals(List.of("b@x.com"), emails(ProfileSpecs.cityIs("pune").and(ProfileSpecs.nameContains("BOB")), page));
        assertEquals(3, emails(ProfileSpecs.cityIs(null).and(ProfileSpecs.nameContains(null)), page).size());
    }

    private List<String> emails(org.springframework.data.jpa.domain.Specification<Profile> spec, PageRequest page) {
        return repo.findAll(spec, page).map(Profile::getEmail).getContent();
    }
}
