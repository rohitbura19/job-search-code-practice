package practice.e;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TaskStatus status = TaskStatus.TODO;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Comment> comments = new ArrayList<>();

    protected Task() {}

    public Task(String title) { this.title = title; }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<Comment> getComments() { return comments; }

    /** Keep both sides of the association in sync. */
    public Comment addComment(String author, String text) {
        Comment c = new Comment(this, author, text);
        comments.add(c);
        return c;
    }

    /** @return true if a comment with that id existed (orphanRemoval then deletes the row). */
    public boolean removeComment(Long commentId) {
        return comments.removeIf(c -> commentId.equals(c.getId()));
    }
}
