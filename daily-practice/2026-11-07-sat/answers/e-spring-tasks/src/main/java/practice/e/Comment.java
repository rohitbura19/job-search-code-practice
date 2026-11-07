package practice.e;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(nullable = false, length = 50)
    private String author;

    @Column(nullable = false, length = 500)
    private String text;

    protected Comment() {}

    Comment(Task task, String author, String text) { this.task = task; this.author = author; this.text = text; }

    public Long getId() { return id; }
    public Task getTask() { return task; }
    public String getAuthor() { return author; }
    public String getText() { return text; }
}
