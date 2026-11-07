package practice.e;

import jakarta.persistence.*;

/**
 * TODO(E2): map this entity: table "comments", IDENTITY id, many-to-one to Task (lazy, required, join column "task_id"),
 * author not null length 50, text not null length 500.
 */
@Entity
public class Comment {
    @Id
    private Long id; // TODO(E2): generation strategy

    private Task task;

    private String author;

    private String text;

    protected Comment() {}

    Comment(Task task, String author, String text) { this.task = task; this.author = author; this.text = text; }

    public Long getId() { return id; }
    public Task getTask() { return task; }
    public String getAuthor() { return author; }
    public String getText() { return text; }
}
