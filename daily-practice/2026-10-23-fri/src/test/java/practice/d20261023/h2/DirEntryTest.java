package practice.d20261023.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DirEntryTest {
    static DirEntry sample() {
        return new DirEntry("root")
                .add(new FileEntry("x.txt", 10))
                .add(new DirEntry("sub").add(new FileEntry("y.txt", 30)).add(new FileEntry("a.txt", 30)))
                .add(new DirEntry("empty"));
    }

    @Test void sizeIsTheSumBelow() {
        assertEquals(70, sample().size());
        assertEquals(0, new DirEntry("e").size());
    }

    @Test void sizeIsLive() {
        DirEntry sub = new DirEntry("sub");
        DirEntry root = new DirEntry("root").add(sub);
        assertEquals(0, root.size());
        sub.add(new FileEntry("late", 5));
        assertEquals(5, root.size());
    }

    @Test void pathsAreSortedFilesOnly() {
        assertEquals(List.of("sub/a.txt", "sub/y.txt", "x.txt"), sample().paths());
        assertEquals(List.of(), new DirEntry("e").paths());
    }

    @Test void largestFileTieGoesToSmallerPath() {
        assertEquals(Optional.of(new FileEntry("a.txt", 30)), sample().largestFile());
        assertEquals(Optional.empty(), new DirEntry("e").add(new DirEntry("f")).largestFile());
    }

    @Test void childrenAreSortedAndASnapshot() {
        DirEntry d = sample();
        List<Entry> kids = d.children();
        assertEquals(List.of("empty", "sub", "x.txt"), kids.stream().map(Entry::name).toList());
        assertThrows(UnsupportedOperationException.class, () -> kids.add(new FileEntry("z", 1)));
        d.add(new FileEntry("zz", 1));
        assertEquals(3, kids.size(), "an earlier snapshot does not change");
        assertEquals(4, d.children().size());
    }

    @Test void duplicateNamesAreRejectedEvenAcrossKinds() {
        DirEntry d = new DirEntry("d").add(new FileEntry("n", 1));
        assertThrows(IllegalArgumentException.class, () -> d.add(new FileEntry("n", 2)));
        assertThrows(IllegalArgumentException.class, () -> d.add(new DirEntry("n")));
        assertEquals(1, d.size());
    }

    @Test void cyclesAreRejected() {
        DirEntry a = new DirEntry("a"), b = new DirEntry("b"), c = new DirEntry("c");
        a.add(b);
        b.add(c);
        assertThrows(IllegalArgumentException.class, () -> a.add(a));
        assertThrows(IllegalArgumentException.class, () -> c.add(a));
        assertThrows(IllegalArgumentException.class, () -> b.add(a));
        assertEquals(List.of(), a.paths());
    }

    @Test void invalidArguments() {
        assertThrows(NullPointerException.class, () -> new DirEntry("d").add(null));
        for (String bad : new String[]{null, "", "  ", "a/b"})
            assertThrows(IllegalArgumentException.class, () -> new DirEntry(bad), String.valueOf(bad));
    }

    @Test void largeFlatDirectory() {
        DirEntry d = new DirEntry("big");
        for (int i = 0; i < 100_000; i++) d.add(new FileEntry("f" + i, i));
        assertEquals(99_999L * 100_000 / 2, d.size());
        assertEquals(new FileEntry("f99999", 99_999), d.largestFile().orElseThrow());
    }
}
