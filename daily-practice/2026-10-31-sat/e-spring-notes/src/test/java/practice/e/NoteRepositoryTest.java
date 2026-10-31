package practice.e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest
class NoteRepositoryTest {
    @Autowired NoteRepository repo;
    @Autowired EntityManager em;

    Note a, b;

    @BeforeEach void seed() {
        a = repo.saveAndFlush(new Note("ann", "Groceries", "milk, eggs"));
        b = repo.saveAndFlush(new Note("ann", "Trip plan", "Book FLIGHTS and hotel"));
        repo.saveAndFlush(new Note("bob", "Groceries", "bread"));
        em.clear();
    }

    @Test void createdAtAndVersionAreMaintained() {
        Note n = repo.findById(a.getId()).orElseThrow();
        assertNotNull(n.getCreatedAt());
        assertEquals(0, n.getVersion());
        n.edit("Groceries 2", "milk");
        repo.saveAndFlush(n);
        assertEquals(1, n.getVersion());
    }

    @Test void uniqueOwnerAndTitle() {
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(new Note("ann", "Groceries", "again")));
    }

    @Test void derivedQueries() {
        assertEquals(2, repo.countByOwner("ann"));
        assertEquals(0, repo.countByOwner("nobody"));
        var page = repo.findByOwner("ann", PageRequest.of(0, 1, Sort.by("id")));
        assertEquals(2, page.getTotalElements());
        assertEquals("Groceries", page.getContent().get(0).getTitle());
    }

    @Test void searchIsCaseInsensitiveOnTitleAndBodyAndScopedToOwner() {
        var byBody = repo.search("ann", "flights", PageRequest.of(0, 10));
        assertEquals(1, byBody.getTotalElements());
        assertEquals("Trip plan", byBody.getContent().get(0).getTitle());
        assertEquals(1, repo.search("ann", "GROCER", PageRequest.of(0, 10)).getTotalElements());
        assertEquals(1, repo.search("bob", "grocer", PageRequest.of(0, 10)).getTotalElements());
        assertEquals(0, repo.search("bob", "flights", PageRequest.of(0, 10)).getTotalElements());
    }

    @Test void deleteIsSoft() {
        repo.delete(repo.findById(a.getId()).orElseThrow());
        repo.flush();
        em.clear();
        assertTrue(repo.findById(a.getId()).isEmpty());                         // filtered from find
        assertEquals(1, repo.countByOwner("ann"));                              // and from derived queries
        assertEquals(1, repo.findAll().stream().filter(n -> n.getOwner().equals("ann")).count());
        Number rows = (Number) em.createNativeQuery("select count(*) from notes where deleted = true").getSingleResult();
        assertEquals(1, rows.intValue());                                       // the row is still there
    }

    @Test void restoreBringsItBack() {
        repo.delete(repo.findById(a.getId()).orElseThrow());
        repo.flush();
        assertEquals(1, repo.restore(a.getId()));
        assertEquals("Groceries", repo.findById(a.getId()).orElseThrow().getTitle());
        assertEquals(0, repo.restore(999_999));
    }

    @Test void deletedTitleStillBlocksReuse() {
        repo.delete(repo.findById(b.getId()).orElseThrow());
        repo.flush();
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(new Note("ann", "Trip plan", "x")));
    }
}
