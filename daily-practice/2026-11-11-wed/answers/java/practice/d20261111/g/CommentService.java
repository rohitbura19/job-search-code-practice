package practice.d20261111.g;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/** Each comment is a mutable node (private) with: parent link, ordered child ids, votes map userId -> +1/-1, and liveInSubtree = number of non-deleted comments in its subtree
 *  (itself included). add/delete walk the ancestor chain (O(depth), depth is capped) to maintain it, so "visible" is just liveInSubtree > 0.
 *  One lock (synchronized) keeps every operation atomic; reads return immutable Comment snapshots. A production version would shard by postId. */
public class CommentService {
    private static final String DELETED = "[deleted]";

    private static final class Node {
        final long id, postId; final Long parentId; final String author; final Instant createdAt; final int depth;
        String body; boolean deleted; int score; int liveInSubtree = 1;
        final Map<String, Integer> votes = new HashMap<>();
        final List<Long> children = new ArrayList<>();

        Node(long id, long postId, Long parentId, String author, String body, Instant createdAt, int depth) {
            this.id = id; this.postId = postId; this.parentId = parentId; this.author = author; this.body = body; this.createdAt = createdAt; this.depth = depth;
        }

        Comment snapshot() { return new Comment(id, postId, parentId, author, body, createdAt, score, deleted, depth); }
    }

    private final Clock clock;
    private final int maxDepth, maxBodyLength;
    private final Map<Long, Node> nodes = new HashMap<>();
    private final Map<Long, List<Long>> topByPost = new HashMap<>();
    private long nextId = 1;

    public CommentService(Clock clock, int maxDepth, int maxBodyLength) {
        this.clock = clock; this.maxDepth = maxDepth; this.maxBodyLength = maxBodyLength;
    }

    public synchronized Comment add(long postId, Long parentId, String authorId, String body) {
        if (authorId == null || authorId.isBlank()) throw new IllegalArgumentException("author");
        if (body == null || body.isBlank()) throw new IllegalArgumentException("body");
        String text = body.strip();
        if (text.length() > maxBodyLength) throw new IllegalArgumentException("body too long");
        int depth = 0;
        Node parent = null;
        if (parentId != null) {
            parent = node(parentId);
            if (parent.postId != postId) throw new IllegalArgumentException("parent belongs to another post");
            if (parent.deleted) throw new IllegalStateException("parent deleted");
            if (parent.depth + 1 > maxDepth) throw new IllegalStateException("max depth reached");
            depth = parent.depth + 1;
        }
        Node n = new Node(nextId++, postId, parentId, authorId, text, clock.instant(), depth);
        nodes.put(n.id, n);
        if (parent == null) topByPost.computeIfAbsent(postId, k -> new ArrayList<>()).add(n.id);
        else parent.children.add(n.id);
        adjustAncestors(parent, +1);
        return n.snapshot();
    }

    public synchronized Comment vote(long commentId, String userId, int direction) {
        if (direction < -1 || direction > 1) throw new IllegalArgumentException("direction");
        Node n = node(commentId);
        if (n.deleted) throw new IllegalStateException("deleted");
        if (n.author.equals(userId)) throw new IllegalStateException("own comment");
        int old = n.votes.getOrDefault(userId, 0);
        if (direction == 0) n.votes.remove(userId); else n.votes.put(userId, direction);
        n.score += direction - old;
        return n.snapshot();
    }

    public synchronized void delete(long commentId, String userId) {
        Node n = node(commentId);
        if (!n.author.equals(userId)) throw new IllegalStateException("not the author");
        if (n.deleted) return;
        n.deleted = true;
        n.body = DELETED;
        adjustAncestors(n, -1);
    }

    public synchronized List<Comment> topLevel(long postId, CommentSort sort, int offset, int limit) {
        if (offset < 0 || limit < 0) throw new IllegalArgumentException("paging");
        Comparator<Node> order = sort == CommentSort.NEW
                ? Comparator.<Node, Instant>comparing(x -> x.createdAt).thenComparingLong(x -> x.id).reversed()
                : Comparator.<Node>comparingInt(x -> -x.score).thenComparing(x -> x.createdAt).thenComparingLong(x -> x.id);
        return topByPost.getOrDefault(postId, List.of()).stream().map(nodes::get).filter(x -> x.liveInSubtree > 0)
                .sorted(order).skip(offset).limit(limit).map(Node::snapshot).toList();
    }

    public synchronized List<Comment> replies(long commentId) {
        return node(commentId).children.stream().map(nodes::get).filter(x -> x.liveInSubtree > 0)
                .sorted(Comparator.<Node, Instant>comparing(x -> x.createdAt).thenComparingLong(x -> x.id)).map(Node::snapshot).toList();
    }

    public synchronized int countVisible(long postId) {
        int total = 0;
        for (long id : topByPost.getOrDefault(postId, List.of())) total += visibleInSubtree(nodes.get(id));
        return total;
    }

    /** Visible count of a subtree: this node is visible iff liveInSubtree > 0; descendants are checked the same way (iterative, children of invisible nodes are all invisible). */
    private int visibleInSubtree(Node root) {
        int count = 0;
        List<Node> stack = new ArrayList<>(List.of(root));
        while (!stack.isEmpty()) {
            Node n = stack.remove(stack.size() - 1);
            if (n.liveInSubtree == 0) continue;
            count++;
            for (long c : n.children) stack.add(nodes.get(c));
        }
        return count;
    }

    private void adjustAncestors(Node start, int delta) {
        Node cur = start;
        while (cur != null) {
            cur.liveInSubtree += delta;
            cur = cur.parentId == null ? null : nodes.get(cur.parentId);
        }
    }

    private Node node(long id) {
        Node n = nodes.get(id);
        if (n == null) throw new NoSuchElementException("comment " + id);
        return n;
    }
}
