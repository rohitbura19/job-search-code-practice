package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@Import({TaskService.class, AuditingConfig.class, TaskServiceTest.Props.class})
@TestPropertySource(properties = "app.tasks.max-comments-per-task=2")
class TaskServiceTest {
    @TestConfiguration
    @EnableConfigurationProperties(TaskProperties.class)
    static class Props {}

    @Autowired TaskService service;
    @Autowired EntityManager em;

    private Long newTask(String title) {
        Long id = service.create(new CreateTaskRequest(title)).id();
        em.flush();
        em.clear();
        return id;
    }

    @Test void createTrimsAndDefaultsToTodo() {
        TaskView v = service.create(new CreateTaskRequest("  write tests  "));
        assertEquals("write tests", v.title());
        assertEquals(TaskStatus.TODO, v.status());
        assertNotNull(v.createdAt());
        assertTrue(v.comments().isEmpty());
    }

    @Test void getMissingIs404() {
        assertThrows(Exceptions.NotFoundException.class, () -> service.get(999L));
    }

    @Test void commentsRespectConfiguredLimit() {
        Long id = newTask("t");
        CommentView c = service.addComment(id, new CommentRequest(" ann ", " hi "));
        assertNotNull(c.id());
        assertEquals("ann", c.author());
        assertEquals("hi", c.text());
        service.addComment(id, new CommentRequest("bob", "yo"));
        Exceptions.ConflictException e = assertThrows(Exceptions.ConflictException.class,
                () -> service.addComment(id, new CommentRequest("cy", "no")));
        assertTrue(e.getMessage().contains("2"));
        assertEquals(2, service.get(id).comments().size());
    }

    @Test void removeComment() {
        Long id = newTask("t");
        Long cid = service.addComment(id, new CommentRequest("a", "b")).id();
        assertThrows(Exceptions.NotFoundException.class, () -> service.removeComment(id, cid + 100));
        service.removeComment(id, cid);
        em.flush();
        em.clear();
        assertTrue(service.get(id).comments().isEmpty());
        assertThrows(Exceptions.NotFoundException.class, () -> service.removeComment(999L, 1L));
    }

    @Test void statusTransitions() {
        Long id = newTask("t");
        assertEquals(TaskStatus.DOING, service.changeStatus(id, TaskStatus.DOING).status());
        assertEquals(TaskStatus.TODO, service.changeStatus(id, TaskStatus.TODO).status());
        service.changeStatus(id, TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, service.changeStatus(id, TaskStatus.DONE).status()); // same state is idempotent
        assertThrows(Exceptions.ConflictException.class, () -> service.changeStatus(id, TaskStatus.TODO));
        assertThrows(Exceptions.NotFoundException.class, () -> service.changeStatus(999L, TaskStatus.DONE));
    }

    @Test void listSortAndPaging() {
        newTask("b");
        newTask("a");
        newTask("c");
        Page<TaskSummary> byTitle = service.list(null, 0, 2, "title");
        assertEquals(3, byTitle.getTotalElements());
        assertEquals(List.of("a", "b"), byTitle.map(TaskSummary::title).getContent());
        assertEquals(List.of("c", "b"), service.list(null, 0, 2, "title,desc").map(TaskSummary::title).getContent());
        assertEquals(List.of("b", "a"), service.list(null, 0, 2, null).map(TaskSummary::title).getContent()); // default: id ascending
        assertEquals(List.of("a", "c"), service.list(null, 0, 2, "createdAt,desc").map(TaskSummary::title).getContent().stream().sorted().toList());
    }

    @Test void listFiltersByStatus() {
        Long id = newTask("a");
        newTask("b");
        service.changeStatus(id, TaskStatus.DOING);
        assertEquals(List.of("a"), service.list(TaskStatus.DOING, 0, 10, null).map(TaskSummary::title).getContent());
    }

    @Test void sortIsWhitelisted() {
        for (String bad : new String[]{"password", "id; drop table tasks", "title,sideways", "title,asc,extra", "status"})
            assertThrows(IllegalArgumentException.class, () -> service.list(null, 0, 5, bad), bad);
    }

    @Test void statsAreZeroFilledInEnumOrder() {
        assertEquals(Map.of(TaskStatus.TODO, 0L, TaskStatus.DOING, 0L, TaskStatus.DONE, 0L), service.stats());
        Long id = newTask("a");
        newTask("b");
        service.changeStatus(id, TaskStatus.DONE);
        Map<TaskStatus, Long> s = service.stats();
        assertEquals(List.of(TaskStatus.TODO, TaskStatus.DOING, TaskStatus.DONE), List.copyOf(s.keySet()));
        assertEquals(1L, s.get(TaskStatus.TODO));
        assertEquals(1L, s.get(TaskStatus.DONE));
    }
}
