package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
@Import(AuditingConfig.class)
class TaskRepositoryTest {
    @Autowired TaskRepository repo;
    @Autowired EntityManager em;
    @Autowired JdbcTemplate jdbc;

    private Task save(String title, TaskStatus status) {
        Task t = new Task(title);
        t.setStatus(status);
        return repo.saveAndFlush(t);
    }

    @Test void auditingFillsTimestampsAndUpdatesModified() throws Exception {
        Task t = save("a", TaskStatus.TODO);
        assertNotNull(t.getCreatedAt());
        assertNotNull(t.getUpdatedAt());
        var created = t.getCreatedAt();
        Thread.sleep(5);
        t.setStatus(TaskStatus.DOING);
        repo.saveAndFlush(t);
        assertEquals(created, t.getCreatedAt());
        assertTrue(t.getUpdatedAt().isAfter(created));
    }

    @Test void statusIsStoredAsText() {
        Task t = save("a", TaskStatus.DOING);
        assertEquals("DOING", jdbc.queryForObject("select status from tasks where id = ?", String.class, t.getId()));
    }

    @Test void findWithCommentsInitialisesCollectionInIdOrder() {
        Task t = new Task("a");
        t.addComment("x", "first");
        t.addComment("y", "second");
        Long id = repo.saveAndFlush(t).getId();
        em.clear();
        Task loaded = repo.findWithComments(id).orElseThrow();
        assertTrue(Hibernate.isInitialized(loaded.getComments()));
        assertEquals(List.of("first", "second"), loaded.getComments().stream().map(Comment::getText).toList());
        assertTrue(repo.findWithComments(id + 1000).isEmpty());
    }

    @Test void removingFromTheCollectionDeletesTheRow() {
        Task t = new Task("a");
        Comment c = t.addComment("x", "hi");
        t.addComment("y", "yo");
        repo.saveAndFlush(t);
        assertTrue(t.removeComment(c.getId()));
        assertFalse(t.removeComment(c.getId()));
        repo.flush();
        assertEquals(1, jdbc.queryForObject("select count(*) from comments", Integer.class));
    }

    @Test void deletingTheTaskCascadesToComments() {
        Task t = new Task("a");
        t.addComment("x", "hi");
        repo.saveAndFlush(t);
        repo.delete(t);
        repo.flush();
        assertEquals(0, jdbc.queryForObject("select count(*) from comments", Integer.class));
    }

    @Test void pagingByStatusWithSort() {
        save("b", TaskStatus.TODO);
        save("a", TaskStatus.TODO);
        save("c", TaskStatus.TODO);
        save("z", TaskStatus.DONE);
        Page<Task> p = repo.findByStatus(TaskStatus.TODO, PageRequest.of(0, 2, Sort.by("title")));
        assertEquals(3, p.getTotalElements());
        assertEquals(List.of("a", "b"), p.getContent().stream().map(Task::getTitle).toList());
        Page<Task> p2 = repo.findByStatus(TaskStatus.TODO, PageRequest.of(1, 2, Sort.by("title")));
        assertEquals(List.of("c"), p2.getContent().stream().map(Task::getTitle).toList());
        assertTrue(repo.findByStatus(TaskStatus.DOING, PageRequest.of(0, 5)).isEmpty());
    }

    @Test void countByStatusProjection() {
        save("a", TaskStatus.TODO);
        save("b", TaskStatus.TODO);
        save("c", TaskStatus.DONE);
        List<StatusCount> counts = repo.countByStatus();
        assertEquals(2, counts.size());
        assertEquals(2L, counts.stream().filter(c -> c.getStatus() == TaskStatus.TODO).findFirst().orElseThrow().getTotal());
        assertEquals(1L, counts.stream().filter(c -> c.getStatus() == TaskStatus.DONE).findFirst().orElseThrow().getTotal());
    }
}
