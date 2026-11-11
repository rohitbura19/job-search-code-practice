package practice.d20261111.g;

import java.time.Clock;
import java.util.List;

/**
 * In-memory threaded comments for posts (LLD of F1). Thread-safe. Ids are assigned 1, 2, 3, ... by add().
 *
 * add(postId, parentId, authorId, body) -> Comment
 *   - body must be non-blank after strip() and at most maxBodyLength chars (IllegalArgumentException); stored stripped. authorId non-blank.
 *   - parentId null = top-level; otherwise the parent must exist (NoSuchElementException) and belong to the same post (IllegalArgumentException),
 *     must not be deleted (IllegalStateException) and parent.depth + 1 <= maxDepth (IllegalStateException). createdAt = clock.instant().
 * vote(commentId, userId, direction) -> Comment
 *   - direction in {-1, 0, +1} (else IllegalArgumentException) SETS this user's vote (0 removes it); repeating the same call changes nothing. score = sum of votes.
 *   - the author cannot vote on their own comment (IllegalStateException); a deleted comment cannot be voted on (IllegalStateException); unknown id -> NoSuchElementException.
 * delete(commentId, userId): only the author (else IllegalStateException); unknown id -> NoSuchElementException. Soft delete: body becomes "[deleted]", deleted = true,
 *   votes are kept but no longer change; deleting twice is a no-op. The replies stay.
 * topLevel(postId, sort, offset, limit) -> visible top-level comments in that order, a page of at most `limit` starting at `offset` (offset/limit >= 0, else IllegalArgumentException).
 * replies(commentId) -> visible direct children, oldest first (createdAt, then id). Unknown id -> NoSuchElementException.
 * countVisible(postId) -> number of visible comments of the post.
 * VISIBLE = not deleted, OR deleted but with at least one non-deleted descendant (so the thread keeps its shape). Make this check O(depth), not a subtree walk.
 */
public class CommentService {
    public CommentService(Clock clock, int maxDepth, int maxBodyLength) {}

    public Comment add(long postId, Long parentId, String authorId, String body) { throw new UnsupportedOperationException("TODO"); }
    public Comment vote(long commentId, String userId, int direction) { throw new UnsupportedOperationException("TODO"); }
    public void delete(long commentId, String userId) { throw new UnsupportedOperationException("TODO"); }
    public List<Comment> topLevel(long postId, CommentSort sort, int offset, int limit) { throw new UnsupportedOperationException("TODO"); }
    public List<Comment> replies(long commentId) { throw new UnsupportedOperationException("TODO"); }
    public int countVisible(long postId) { throw new UnsupportedOperationException("TODO"); }
}
