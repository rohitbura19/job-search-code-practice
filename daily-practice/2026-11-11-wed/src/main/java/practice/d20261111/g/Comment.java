package practice.d20261111.g;

import java.time.Instant;

/** Given. Immutable snapshot returned by the service. parentId is null for a top-level comment, depth is 0 for top-level. A deleted comment has body "[deleted]". */
public record Comment(long id, long postId, Long parentId, String authorId, String body, Instant createdAt, int score, boolean deleted, int depth) {}
