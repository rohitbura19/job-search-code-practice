package practice.e;

import java.time.Instant;
import java.util.Set;

/**
 * TODO: JPA entity, table "articles".
 *   id (generated, IDENTITY); url (not null, unique, max 300); title (not null, max 120);
 *   tags: a Set<String> stored in table "article_tags" (columns article_id, tag max 20) - @ElementCollection, LAZY, NOT a separate entity;
 *   createdAt: Instant set by Hibernate on insert, never updated.
 * Needs a protected no-arg constructor; the (url, title, tags) constructor and the getters are given as stubs - make them work.
 * getTags() returns an unmodifiable view.
 */
public class Article {
    protected Article() {}

    public Article(String url, String title, Set<String> tags) { throw new UnsupportedOperationException("TODO"); }

    public Long getId() { throw new UnsupportedOperationException("TODO"); }
    public String getUrl() { throw new UnsupportedOperationException("TODO"); }
    public String getTitle() { throw new UnsupportedOperationException("TODO"); }
    public Set<String> getTags() { throw new UnsupportedOperationException("TODO"); }
    public Instant getCreatedAt() { throw new UnsupportedOperationException("TODO"); }
}
