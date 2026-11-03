package practice.d20261103.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

class FileSystemTest {
    static DirNode sample() {
        return new DirNode("root")
                .add(new FileNode("readme.md", 10))
                .add(new DirNode("src")
                        .add(new FileNode("B.java", 200))
                        .add(new FileNode("A.java", 100))
                        .add(new DirNode("empty")))
                .add(new DirNode("test").add(new FileNode("ATest.java", 50)));
    }

    @Test void sizeIsRecursiveAndLive() {
        DirNode root = sample();
        assertEquals(360, root.size());
        ((DirNode) root.children().get(1)).add(new FileNode("C.txt", 40));    // children sorted: readme.md, src, test
        assertEquals(400, root.size());
        assertEquals(0, new DirNode("e").size());
        assertEquals(7, new FileNode("f", 7).size());
    }

    @Test void childrenAreSortedAndACopy() {
        DirNode root = sample();
        assertEquals(List.of("readme.md", "src", "test"), root.children().stream().map(Node::name).toList());
        root.children().clear();
        assertEquals(3, root.children().size());
    }

    @Test void validation() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode(" ", 1));
        assertThrows(IllegalArgumentException.class, () -> new FileNode("a/b", 1));
        assertThrows(IllegalArgumentException.class, () -> new FileNode("a", -1));
        assertThrows(IllegalArgumentException.class, () -> new DirNode(""));
        assertThrows(IllegalArgumentException.class, () -> new DirNode("a/b"));
        DirNode d = new DirNode("d").add(new FileNode("x", 1));
        assertThrows(IllegalArgumentException.class, () -> d.add(new FileNode("x", 2)));
        assertThrows(IllegalArgumentException.class, () -> d.add(new DirNode("x")));
    }

    @Test void oneParentAndNoCycles() {
        DirNode a = new DirNode("a"), b = new DirNode("b"), c = new DirNode("c");
        FileNode f = new FileNode("f", 1);
        a.add(b);
        b.add(c);
        assertThrows(IllegalStateException.class, () -> a.add(a));
        assertThrows(IllegalStateException.class, () -> c.add(a));          // a is an ancestor of c
        assertThrows(IllegalStateException.class, () -> new DirNode("z").add(b));   // b already has a parent
        a.add(f);
        assertThrows(IllegalStateException.class, () -> b.add(f));
        assertEquals(1, a.size());
    }

    @Test void fileCountVisitor() {
        assertEquals(4, sample().accept(Visitors.fileCount()));
        assertEquals(1, new FileNode("f", 0).accept(Visitors.fileCount()));
        assertEquals(0, new DirNode("e").accept(Visitors.fileCount()));
    }

    @Test void pathsVisitor() {
        assertEquals(List.of("root/src/A.java", "root/src/B.java", "root/test/ATest.java"), sample().accept(Visitors.paths(".java")));
        assertEquals(List.of("root/readme.md"), sample().accept(Visitors.paths(".md")));
        assertEquals(List.of(), sample().accept(Visitors.paths(".kt")));
        assertEquals(List.of("f.txt"), new FileNode("f.txt", 1).accept(Visitors.paths("")));
    }

    @Test void renderVisitor() {
        String expected = String.join("\n",
                "root/",
                "  readme.md (10)",
                "  src/",
                "    A.java (100)",
                "    B.java (200)",
                "    empty/",
                "  test/",
                "    ATest.java (50)");
        assertEquals(expected, sample().accept(Visitors.render()));
        assertEquals("f (3)", new FileNode("f", 3).accept(Visitors.render()));
        assertEquals("e/", new DirNode("e").accept(Visitors.render()));
    }

    @Test void visitorIsReusable() {
        Visitor<String> r = Visitors.render();
        assertEquals(sample().accept(r), sample().accept(r));
    }

    @Test void wideTreeWithinTimeout() {
        DirNode root = new DirNode("r");
        for (int i = 0; i < 2000; i++) {
            DirNode d = new DirNode(String.format("d%04d", i));
            for (int j = 0; j < 50; j++) d.add(new FileNode("f" + j + ".txt", 1));
            root.add(d);
        }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertEquals(100_000, root.size());
            assertEquals(100_000, root.accept(Visitors.fileCount()));
            assertEquals(100_000, root.accept(Visitors.paths(".txt")).size());
        });
    }
}
