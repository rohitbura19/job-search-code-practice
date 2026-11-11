package practice.d20261111.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentServiceTest {
    static class MutableClock extends Clock {
        Instant now = Instant.parse("2026-11-11T08:00:00Z");
        void tick() { now = now.plusSeconds(60); }
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId z) { return this; }
        @Override public Instant instant() { return now; }
    }

    MutableClock clock;
    CommentService svc;

    @BeforeEach void setUp() { clock = new MutableClock(); svc = new CommentService(clock, 3, 50); }

    Comment add(Long parent, String author, String body) { clock.tick(); return svc.add(1, parent, author, body); }

    @Test void addAssignsIdsDepthsAndStripsBody() {
        Comment a = add(null, "ann", "  hello  ");
        Comment b = add(a.id(), "bob", "reply");
        assertEquals(1, a.id());
        assertEquals("hello", a.body());
        assertNull(a.parentId());
        assertEquals(0, a.depth());
        assertEquals(2, b.id());
        assertEquals(a.id(), b.parentId());
        assertEquals(1, b.depth());
        assertEquals(0, b.score());
        assertFalse(b.deleted());
    }

    @Test void addValidation() {
        assertThrows(IllegalArgumentException.class, () -> svc.add(1, null, "ann", "   "));
        assertThrows(IllegalArgumentException.class, () -> svc.add(1, null, "ann", null));
        assertThrows(IllegalArgumentException.class, () -> svc.add(1, null, " ", "x"));
        assertThrows(IllegalArgumentException.class, () -> svc.add(1, null, "ann", "x".repeat(51)));
        assertEquals(50, add(null, "ann", "x".repeat(50)).body().length());
        assertThrows(NoSuchElementException.class, () -> svc.add(1, 99L, "ann", "x"));
        Comment c = add(null, "ann", "top");
        assertThrows(IllegalArgumentException.class, () -> svc.add(2, c.id(), "bob", "wrong post"));
    }

    @Test void depthLimitAndDeletedParent() {
        Comment c0 = add(null, "a", "0");
        Comment c1 = add(c0.id(), "a", "1");
        Comment c2 = add(c1.id(), "a", "2");
        Comment c3 = add(c2.id(), "a", "3");
        assertEquals(3, c3.depth());
        assertThrows(IllegalStateException.class, () -> add(c3.id(), "a", "4"));
        svc.delete(c1.id(), "a");
        assertThrows(IllegalStateException.class, () -> add(c1.id(), "b", "reply to deleted"));
    }

    @Test void voteIsASetAndNotAnIncrement() {
        Comment c = add(null, "ann", "post");
        assertEquals(1, svc.vote(c.id(), "bob", 1).score());
        assertEquals(1, svc.vote(c.id(), "bob", 1).score());
        assertEquals(2, svc.vote(c.id(), "cy", 1).score());
        assertEquals(0, svc.vote(c.id(), "bob", -1).score());
        assertEquals(1, svc.vote(c.id(), "bob", 0).score());
        assertEquals(1, svc.vote(c.id(), "bob", 0).score());
    }

    @Test void voteRules() {
        Comment c = add(null, "ann", "post");
        assertThrows(IllegalStateException.class, () -> svc.vote(c.id(), "ann", 1));
        assertThrows(IllegalArgumentException.class, () -> svc.vote(c.id(), "bob", 2));
        assertThrows(NoSuchElementException.class, () -> svc.vote(77, "bob", 1));
        svc.delete(c.id(), "ann");
        assertThrows(IllegalStateException.class, () -> svc.vote(c.id(), "bob", 1));
    }

    @Test void deleteRulesAndIdempotence() {
        Comment c = add(null, "ann", "secret");
        assertThrows(IllegalStateException.class, () -> svc.delete(c.id(), "bob"));
        assertThrows(NoSuchElementException.class, () -> svc.delete(42, "ann"));
        add(c.id(), "bob", "keeps the thread alive");
        svc.delete(c.id(), "ann");
        svc.delete(c.id(), "ann");
        Comment shown = svc.topLevel(1, CommentSort.NEW, 0, 10).get(0);
        assertTrue(shown.deleted());
        assertEquals("[deleted]", shown.body());
    }

    @Test void deletedLeafIsHiddenButDeletedParentWithLiveReplyStays() {
        Comment top = add(null, "ann", "top");
        Comment mid = add(top.id(), "bob", "mid");
        Comment leaf = add(mid.id(), "cy", "leaf");
        assertEquals(3, svc.countVisible(1));
        svc.delete(top.id(), "ann");
        svc.delete(mid.id(), "bob");
        assertEquals(3, svc.countVisible(1));                      // top and mid are tombstones above a live leaf: all three stay visible
        assertEquals(1, svc.topLevel(1, CommentSort.NEW, 0, 10).size());
        assertEquals(List.of(mid.id()), svc.replies(top.id()).stream().map(Comment::id).toList());
        svc.delete(leaf.id(), "cy");
        assertEquals(0, svc.countVisible(1));
        assertTrue(svc.topLevel(1, CommentSort.NEW, 0, 10).isEmpty());
        assertTrue(svc.replies(top.id()).isEmpty());
        Comment other = add(null, "dee", "new thread");
        assertEquals(1, svc.countVisible(1));
        assertEquals(List.of(other.id()), svc.topLevel(1, CommentSort.TOP, 0, 10).stream().map(Comment::id).toList());
    }

    @Test void sortOrdersAndPaging() {
        Comment c1 = add(null, "a", "one");
        Comment c2 = add(null, "b", "two");
        Comment c3 = add(null, "c", "three");
        Comment c4 = add(null, "d", "four");
        svc.vote(c2.id(), "x", 1); svc.vote(c2.id(), "y", 1);
        svc.vote(c3.id(), "x", 1); svc.vote(c3.id(), "y", 1);
        svc.vote(c4.id(), "x", -1);
        assertEquals(List.of(4L, 3L, 2L, 1L), ids(svc.topLevel(1, CommentSort.NEW, 0, 10)));
        assertEquals(List.of(2L, 3L, 1L, 4L), ids(svc.topLevel(1, CommentSort.TOP, 0, 10)));   // score desc, then older first
        assertEquals(List.of(3L, 1L), ids(svc.topLevel(1, CommentSort.TOP, 1, 2)));
        assertEquals(List.of(), ids(svc.topLevel(1, CommentSort.TOP, 4, 2)));
        assertEquals(List.of(), ids(svc.topLevel(1, CommentSort.TOP, 0, 0)));
        assertEquals(List.of(), ids(svc.topLevel(99, CommentSort.NEW, 0, 5)));
        assertThrows(IllegalArgumentException.class, () -> svc.topLevel(1, CommentSort.NEW, -1, 5));
        assertThrows(IllegalArgumentException.class, () -> svc.topLevel(1, CommentSort.NEW, 0, -1));
    }

    @Test void sameTimestampTieBreaksOnId() {
        Comment a = svc.add(1, null, "a", "x");
        Comment b = svc.add(1, null, "b", "y");                    // same instant
        assertEquals(List.of(b.id(), a.id()), ids(svc.topLevel(1, CommentSort.NEW, 0, 5)));
        assertEquals(List.of(a.id(), b.id()), ids(svc.topLevel(1, CommentSort.TOP, 0, 5)));
    }

    @Test void repliesOldestFirstAndPostsAreIsolated() {
        Comment top = add(null, "a", "t");
        Comment r1 = add(top.id(), "b", "r1");
        Comment r2 = add(top.id(), "c", "r2");
        svc.add(2, null, "z", "other post");
        assertEquals(List.of(r1.id(), r2.id()), ids(svc.replies(top.id())));
        assertEquals(3, svc.countVisible(1));
        assertEquals(1, svc.countVisible(2));
        assertThrows(NoSuchElementException.class, () -> svc.replies(500));
    }

    @Test void concurrentVotesFromDistinctUsersAllCount() throws Exception {
        Comment c = add(null, "author", "popular");
        ExecutorService ex = Executors.newFixedThreadPool(8);
        List<Future<?>> fs = new ArrayList<>();
        for (int u = 0; u < 2000; u++) { String user = "u" + u; fs.add(ex.submit(() -> svc.vote(c.id(), user, 1))); }
        for (Future<?> f : fs) f.get(10, TimeUnit.SECONDS);
        ex.shutdown();
        assertEquals(2000, svc.topLevel(1, CommentSort.TOP, 0, 1).get(0).score());
    }

    @Test void visibilityStaysConsistentUnderRandomOperations() {
        Random r = new Random(31);
        CommentService s = new CommentService(clock, 4, 20);
        List<Comment> all = new ArrayList<>();
        Map<Long, Boolean> deleted = new HashMap<>();
        Map<Long, Long> parent = new HashMap<>();
        for (int i = 0; i < 400; i++) {
            clock.tick();
            if (all.isEmpty() || r.nextInt(3) > 0) {
                Long p = all.isEmpty() || r.nextInt(4) == 0 ? null : all.get(r.nextInt(all.size())).id();
                try { Comment c = s.add(1, p, "u" + r.nextInt(5), "t"); all.add(c); deleted.put(c.id(), false); parent.put(c.id(), p); }
                catch (IllegalStateException expectedWhenParentDeletedOrTooDeep) { }
            } else {
                Comment c = all.get(r.nextInt(all.size()));
                s.delete(c.id(), c.authorId());
                deleted.put(c.id(), true);
            }
        }
        int expected = 0;
        for (Comment c : all) if (visible(c.id(), all, deleted, parent)) expected++;
        assertEquals(expected, s.countVisible(1));
    }

    private static boolean visible(long id, List<Comment> all, Map<Long, Boolean> deleted, Map<Long, Long> parent) {
        if (!deleted.get(id)) return true;
        for (Comment c : all) if (parent.get(c.id()) != null && parent.get(c.id()) == id && visible(c.id(), all, deleted, parent)) return true;
        return false;
    }

    private static List<Long> ids(List<Comment> l) { return l.stream().map(Comment::id).toList(); }
}
