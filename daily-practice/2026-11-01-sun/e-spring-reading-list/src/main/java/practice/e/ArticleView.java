package practice.e;

import java.time.Instant;
import java.util.SortedSet;
import java.util.TreeSet;

public record ArticleView(long id, String url, String title, SortedSet<String> tags, Instant createdAt) {
    static ArticleView of(Article a) { return new ArticleView(a.getId(), a.getUrl(), a.getTitle(), new TreeSet<>(a.getTags()), a.getCreatedAt()); }
}
