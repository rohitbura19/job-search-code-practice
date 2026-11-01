package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest
class ArticleRepositoryTest {
    @Autowired ArticleRepository repo;
    @Autowired EntityManager em;

    Article a1, a2, a3;

    @BeforeEach void seed() {
        a1 = repo.saveAndFlush(new Article("https://a.example/1", "One", Set.of("java", "spring")));
        a2 = repo.saveAndFlush(new Article("https://a.example/2", "Two", Set.of("java")));
        a3 = repo.saveAndFlush(new Article("https://a.example/3", "Three", Set.of("kafka", "spring", "java")));
        em.clear();
    }

    @Test void createdAtIsSetAndTagsArePersisted() {
        Article a = repo.findById(a3.getId()).orElseThrow();
        assertNotNull(a.getCreatedAt());
        assertEquals(Set.of("kafka", "spring", "java"), a.getTags());   // lazy load inside the test transaction
        assertThrows(UnsupportedOperationException.class, () -> a.getTags().add("x"));
    }

    @Test void urlIsUnique() {
        assertTrue(repo.existsByUrl("https://a.example/1"));
        assertFalse(repo.existsByUrl("https://a.example/9"));
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(new Article("https://a.example/1", "Dup", Set.of())));
    }

    @Test void findByTagPagesAndCounts() {
        var page = repo.findByTag("spring", PageRequest.of(0, 1, Sort.by("id")));
        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
        assertEquals(a1.getId(), page.getContent().get(0).getId());
        var second = repo.findByTag("spring", PageRequest.of(1, 1, Sort.by("id")));
        assertEquals(a3.getId(), second.getContent().get(0).getId());
        assertEquals(3, repo.findByTag("java", PageRequest.of(0, 10)).getTotalElements());
        assertEquals(0, repo.findByTag("nope", PageRequest.of(0, 10)).getTotalElements());
    }

    @Test void tagCountsOrderedByCountThenTag() {
        List<TagCount> counts = repo.tagCounts();
        assertEquals(List.of("java", "spring", "kafka"), counts.stream().map(TagCount::getTag).toList());
        assertEquals(List.of(3L, 2L, 1L), counts.stream().map(TagCount::getTotal).toList());
    }

    @Test void newestThreeComeWithTagsLoadedByTheEntityGraph() {
        repo.saveAndFlush(new Article("https://a.example/4", "Four", Set.of("x")));
        em.clear();
        List<Article> latest = repo.findTop3ByOrderByIdDesc();
        assertEquals(List.of("Four", "Three", "Two"), latest.stream().map(Article::getTitle).toList());
        var util = em.getEntityManagerFactory().getPersistenceUnitUtil();
        for (Article a : latest) assertTrue(util.isLoaded(a, "tags"), a.getTitle());
        em.clear();
        assertFalse(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(repo.findById(a1.getId()).orElseThrow(), "tags"));
    }
}
