package practice.d20261101.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryChatServiceTest {
    ChatService chat;

    @BeforeEach void setUp() {
        chat = new InMemoryChatService();
        chat.createConversation("c1", Set.of("ann", "bob"));
    }

    @Test void seqIsGapFreePerConversation() {
        chat.createConversation("c2", Set.of("ann"));
        assertEquals(1, chat.send("c1", "ann", "m1", "hi").seq());
        assertEquals(2, chat.send("c1", "bob", "m1", "hello").seq());   // same clientMsgId, different sender: a new message
        assertEquals(1, chat.send("c2", "ann", "m1", "other").seq());
        assertEquals(3, chat.send("c1", "ann", "m2", "again").seq());
    }

    @Test void retryReturnsOriginalAndAppendsNothing() {
        Message first = chat.send("c1", "ann", "m1", "original");
        Message retry = chat.send("c1", "ann", "m1", "changed text on retry");
        assertEquals(first, retry);
        assertEquals("original", retry.text());
        assertEquals(1, chat.fetch("c1", "bob", 0, 10).size());
    }

    @Test void fetchPagesAscending() {
        for (int i = 1; i <= 5; i++) chat.send("c1", "ann", "m" + i, "t" + i);
        assertEquals(List.of(1L, 2L), chat.fetch("c1", "bob", 0, 2).stream().map(Message::seq).toList());
        assertEquals(List.of(3L, 4L, 5L), chat.fetch("c1", "bob", 2, 10).stream().map(Message::seq).toList());
        assertEquals(List.of(), chat.fetch("c1", "bob", 5, 10));
        assertEquals(List.of(), chat.fetch("c1", "bob", Long.MAX_VALUE, 10));
        assertEquals(5, chat.fetch("c1", "bob", -7, Integer.MAX_VALUE).size());
        assertThrows(IllegalArgumentException.class, () -> chat.fetch("c1", "bob", 0, 0));
    }

    @Test void fetchResultIsASnapshot() {
        chat.send("c1", "ann", "m1", "a");
        List<Message> snap = chat.fetch("c1", "bob", 0, 10);
        chat.send("c1", "ann", "m2", "b");
        assertEquals(1, snap.size());
    }

    @Test void readMarkerIsMonotonicAndBounded() {
        for (int i = 1; i <= 4; i++) chat.send("c1", "ann", "m" + i, "t");
        assertEquals(0, chat.lastRead("c1", "bob"));
        chat.markRead("c1", "bob", 3);
        chat.markRead("c1", "bob", 1);
        assertEquals(3, chat.lastRead("c1", "bob"));
        assertThrows(IllegalArgumentException.class, () -> chat.markRead("c1", "bob", 5));
        chat.markRead("c1", "bob", 4);
        assertEquals(4, chat.lastRead("c1", "bob"));
    }

    @Test void unreadExcludesOwnMessages() {
        chat.send("c1", "ann", "m1", "1");
        chat.send("c1", "bob", "m2", "2");
        chat.send("c1", "ann", "m3", "3");
        assertEquals(2, chat.unreadCount("c1", "bob"));
        assertEquals(1, chat.unreadCount("c1", "ann"));
        chat.markRead("c1", "bob", 2);
        assertEquals(1, chat.unreadCount("c1", "bob"));
        chat.markRead("c1", "bob", 3);
        assertEquals(0, chat.unreadCount("c1", "bob"));
    }

    @Test void validationOrder() {
        assertThrows(IllegalArgumentException.class, () -> chat.send("c1", "ann", "m1", "  "));
        assertThrows(IllegalArgumentException.class, () -> chat.send("c1", "ann", "m1", null));
        assertThrows(IllegalArgumentException.class, () -> chat.send("c1", "ann", " ", "x"));
        assertThrows(IllegalArgumentException.class, () -> chat.send("c1", "ann", "m1", "x".repeat(2001)));
        assertEquals(2000, chat.send("c1", "ann", "m1", "x".repeat(2000)).text().length());
        assertThrows(UnknownConversationException.class, () -> chat.send("nope", "ann", "m1", "x"));
        assertThrows(NotMemberException.class, () -> chat.send("c1", "eve", "m1", "x"));
        assertThrows(NotMemberException.class, () -> chat.fetch("c1", "eve", 0, 5));
        assertThrows(NotMemberException.class, () -> chat.markRead("c1", "eve", 0));
        assertThrows(NotMemberException.class, () -> chat.unreadCount("c1", "eve"));
        assertThrows(UnknownConversationException.class, () -> chat.fetch("nope", "ann", 0, 5));
    }

    @Test void createRules() {
        assertThrows(IllegalStateException.class, () -> chat.createConversation("c1", Set.of("x")));
        assertThrows(IllegalArgumentException.class, () -> chat.createConversation("c3", Set.of()));
        assertThrows(IllegalArgumentException.class, () -> chat.createConversation("c3", null));
        Set<String> members = new HashSet<>(Set.of("zed"));
        chat.createConversation("c4", members);
        members.add("mallory");                                         // caller's later change must not grant access
        assertThrows(NotMemberException.class, () -> chat.send("c4", "mallory", "m1", "x"));
    }

    @Test void parallelSendsProduceUniqueGapFreeSeqs() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
                List<Callable<List<Long>>> jobs = new ArrayList<>();
                for (int t = 0; t < 8; t++) {
                    int id = t;
                    jobs.add(() -> {
                        List<Long> seqs = new ArrayList<>();
                        for (int i = 0; i < 500; i++) seqs.add(chat.send("c1", id % 2 == 0 ? "ann" : "bob", "t" + id + "-" + i, "x").seq());
                        return seqs;
                    });
                }
                Set<Long> all = new HashSet<>();
                for (Future<List<Long>> f : pool.invokeAll(jobs)) all.addAll(f.get());
                assertEquals(4000, all.size());
                for (long s = 1; s <= 4000; s++) assertTrue(all.contains(s), "missing " + s);
            });
        } finally { pool.shutdownNow(); }
    }

    @Test void parallelRetriesOfOneMessageAppendOnce() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            List<Callable<Message>> jobs = new ArrayList<>();
            for (int t = 0; t < 32; t++) jobs.add(() -> chat.send("c1", "ann", "same", "x"));
            Set<Long> seqs = new HashSet<>();
            for (Future<Message> f : pool.invokeAll(jobs)) seqs.add(f.get().seq());
            assertEquals(Set.of(1L), seqs);
            assertEquals(1, chat.fetch("c1", "bob", 0, 100).size());
        } finally { pool.shutdownNow(); }
    }
}
