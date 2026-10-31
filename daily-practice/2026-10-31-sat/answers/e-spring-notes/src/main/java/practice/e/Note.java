package practice.e;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * @SQLDelete replaces the DELETE statement (a versioned entity passes id then version); @SQLRestriction is appended to every
 * query and find on this entity, so soft-deleted rows disappear everywhere except native SQL. The unique constraint still counts
 * soft-deleted rows: a deleted title cannot be reused (trade-off: see the README review).
 */
@Entity
@Table(name = "notes", uniqueConstraints = @UniqueConstraint(columnNames = {"owner", "title"}))
@SQLDelete(sql = "update notes set deleted = true where id = ? and version = ?")
@SQLRestriction("deleted = false")
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 50) private String owner;
    @Column(nullable = false, length = 100) private String title;
    @Column(length = 2000) private String body;
    @CreationTimestamp @Column(nullable = false, updatable = false) private Instant createdAt;
    @Version private long version;
    @Column(nullable = false) private boolean deleted = false;

    protected Note() {}

    public Note(String owner, String title, String body) { this.owner = owner; this.title = title; this.body = body; }

    public Long getId() { return id; }
    public String getOwner() { return owner; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public Instant getCreatedAt() { return createdAt; }
    public long getVersion() { return version; }
    public void edit(String title, String body) { this.title = title; this.body = body; }
}
