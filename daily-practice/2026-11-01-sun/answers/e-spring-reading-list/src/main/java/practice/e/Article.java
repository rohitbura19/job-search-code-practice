package practice.e;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;

/** Element collection = value-typed rows owned by the article (no id, no lifecycle of their own). LAZY by default: load with an @EntityGraph or inside a transaction. */
@Entity
@Table(name = "articles")
public class Article {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 300)
    private String url;

    @Column(nullable = false, length = 120)
    private String title;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "article_tags", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "tag", length = 20)
    private Set<String> tags = new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Article() {}

    public Article(String url, String title, Set<String> tags) {
        this.url = url; this.title = title; this.tags = new HashSet<>(tags);
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public String getTitle() { return title; }
    public Set<String> getTags() { return Collections.unmodifiableSet(tags); }
    public Instant getCreatedAt() { return createdAt; }
}
