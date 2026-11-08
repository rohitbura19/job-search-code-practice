package practice.d20261108.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class EditorTest {
    @Test void insertDeleteUndoRedo() {
        Editor e = new Editor("hello");
        e.run(new InsertCommand(5, " world"));
        assertEquals("hello world", e.text());
        e.run(new DeleteCommand(0, 6));
        assertEquals("world", e.text());
        assertTrue(e.undo());
        assertEquals("hello world", e.text());
        assertTrue(e.undo());
        assertEquals("hello", e.text());
        assertFalse(e.undo());
        assertTrue(e.redo());
        assertTrue(e.redo());
        assertEquals("world", e.text());
        assertFalse(e.redo());
        assertTrue(e.undo());
        assertTrue(e.redo()); // delete re-captures the text, so a second undo still works
        assertTrue(e.undo());
        assertEquals("hello world", e.text());
    }

    @Test void newCommandClearsRedo() {
        Editor e = new Editor("");
        e.run(new InsertCommand(0, "a"));
        e.run(new InsertCommand(1, "b"));
        e.undo();
        assertEquals(1, e.redoDepth());
        e.run(new InsertCommand(1, "c"));
        assertEquals(0, e.redoDepth());
        assertFalse(e.redo());
        assertEquals("ac", e.text());
    }

    @Test void invalidCommandsLeaveStateUntouched() {
        Editor e = new Editor("abc");
        assertThrows(IndexOutOfBoundsException.class, () -> e.run(new InsertCommand(4, "x")));
        assertThrows(IndexOutOfBoundsException.class, () -> e.run(new InsertCommand(-1, "x")));
        assertThrows(IndexOutOfBoundsException.class, () -> e.run(new DeleteCommand(2, 9)));
        assertThrows(IndexOutOfBoundsException.class, () -> e.run(new DeleteCommand(2, 1)));
        assertThrows(IndexOutOfBoundsException.class, () -> e.run(new DeleteCommand(-1, 1)));
        assertEquals("abc", e.text());
        assertEquals(0, e.undoDepth());
        e.run(new DeleteCommand(1, 1)); // empty range is valid
        e.run(new InsertCommand(3, ""));
        assertEquals("abc", e.text());
        assertEquals(2, e.undoDepth());
    }

    @Test void macroIsAtomicAndUndoesAsOne() {
        Editor e = new Editor("abc");
        e.run(new MacroCommand(List.of(new InsertCommand(3, "d"), new DeleteCommand(0, 1), new InsertCommand(0, "XY"))));
        assertEquals("XYbcd", e.text());
        assertEquals(1, e.undoDepth());
        e.undo();
        assertEquals("abc", e.text());
        e.redo();
        assertEquals("XYbcd", e.text());

        Editor f = new Editor("abc");
        assertThrows(IndexOutOfBoundsException.class,
                () -> f.run(new MacroCommand(List.of(new InsertCommand(0, "X"), new DeleteCommand(0, 1), new DeleteCommand(5, 6)))));
        assertEquals("abc", f.text());
        assertEquals(0, f.undoDepth());
    }

    @Test void macroCopiesItsList() {
        List<Command> steps = new ArrayList<>(List.of(new InsertCommand(0, "a")));
        MacroCommand m = new MacroCommand(steps);
        steps.add(new InsertCommand(0, "b"));
        Editor e = new Editor("");
        e.run(m);
        assertEquals("a", e.text());
    }

    @Test void nestedMacro() {
        Command inner = new MacroCommand(List.of(new InsertCommand(0, "1"), new InsertCommand(1, "2")));
        Editor e = new Editor("");
        e.run(new MacroCommand(List.of(inner, new InsertCommand(2, "3"))));
        assertEquals("123", e.text());
        e.undo();
        assertEquals("", e.text());
    }

    @Test void historyLimit() {
        Editor e = new Editor("", 2);
        for (char c : "abcd".toCharArray()) e.run(new InsertCommand(e.text().length(), String.valueOf(c)));
        assertEquals(2, e.undoDepth());
        assertTrue(e.undo());
        assertTrue(e.undo());
        assertFalse(e.undo());
        assertEquals("ab", e.text());
        assertThrows(IllegalArgumentException.class, () -> new Editor("", 0));
    }

    @Test void randomUndoAllRestoresOriginal() {
        Random r = new Random(51);
        for (int t = 0; t < 500; t++) {
            String initial = "abcdef".substring(0, r.nextInt(7));
            Editor e = new Editor(initial);
            List<String> snapshots = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                int len = e.text().length();
                snapshots.add(e.text());
                if (r.nextBoolean()) e.run(new InsertCommand(r.nextInt(len + 1), "xyz".substring(0, r.nextInt(4))));
                else { int a = r.nextInt(len + 1), b = a + r.nextInt(len - a + 1); e.run(new DeleteCommand(a, b)); }
            }
            for (int i = snapshots.size() - 1; i >= 0; i--) { assertTrue(e.undo()); assertEquals(snapshots.get(i), e.text()); }
            assertEquals(initial, e.text());
            for (int i = 0; i < snapshots.size(); i++) assertTrue(e.redo());
            assertFalse(e.redo());
        }
    }
}
