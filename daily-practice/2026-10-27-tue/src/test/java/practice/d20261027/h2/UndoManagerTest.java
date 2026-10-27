package practice.d20261027.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UndoManagerTest {
    TextDocument doc;
    UndoManager mgr;

    @BeforeEach void setUp() {
        doc = new TextDocument();
        mgr = new UndoManager(100);
    }

    @Test void insertUndoRedo() {
        mgr.execute(new InsertCommand(doc, 0, "hello"));
        mgr.execute(new InsertCommand(doc, 5, " world"));
        assertEquals("hello world", doc.text());
        assertTrue(mgr.undo());
        assertEquals("hello", doc.text());
        assertTrue(mgr.redo());
        assertEquals("hello world", doc.text());
    }

    @Test void deleteUndoRestoresTheRemovedText() {
        mgr.execute(new InsertCommand(doc, 0, "abcdef"));
        mgr.execute(new DeleteCommand(doc, 1, 3));
        assertEquals("aef", doc.text());
        mgr.undo();
        assertEquals("abcdef", doc.text());
        mgr.redo();
        assertEquals("aef", doc.text());
    }

    @Test void emptyHistory() {
        assertFalse(mgr.canUndo());
        assertFalse(mgr.canRedo());
        assertFalse(mgr.undo());
        assertFalse(mgr.redo());
    }

    @Test void newCommandClearsRedo() {
        mgr.execute(new InsertCommand(doc, 0, "a"));
        mgr.undo();
        assertTrue(mgr.canRedo());
        mgr.execute(new InsertCommand(doc, 0, "b"));
        assertFalse(mgr.canRedo());
        assertEquals("b", doc.text());
    }

    @Test void failedExecuteLeavesHistoryAndDocumentUntouched() {
        mgr.execute(new InsertCommand(doc, 0, "abc"));
        mgr.undo();
        assertThrows(IndexOutOfBoundsException.class, () -> mgr.execute(new DeleteCommand(doc, 5, 1)));
        assertTrue(mgr.canRedo());                     // redo branch survives a failed command
        assertEquals("", doc.text());
    }

    @Test void historyIsBounded() {
        mgr = new UndoManager(2);
        mgr.execute(new InsertCommand(doc, 0, "a"));
        mgr.execute(new InsertCommand(doc, 1, "b"));
        mgr.execute(new InsertCommand(doc, 2, "c"));
        assertTrue(mgr.undo());
        assertTrue(mgr.undo());
        assertFalse(mgr.undo());
        assertEquals("a", doc.text());
    }

    @Test void macroIsOneHistoryEntryUndoneInReverse() {
        mgr.execute(new InsertCommand(doc, 0, "abcdef"));
        mgr.execute(new MacroCommand(List.of(new DeleteCommand(doc, 0, 2), new InsertCommand(doc, 0, "XY"), new DeleteCommand(doc, 4, 1))));
        assertEquals("XYcdf", doc.text());
        mgr.undo();
        assertEquals("abcdef", doc.text());
        mgr.redo();
        assertEquals("XYcdf", doc.text());
    }

    @Test void macroRollsBackWhenAPartFails() {
        mgr.execute(new InsertCommand(doc, 0, "abc"));
        Command bad = new MacroCommand(List.of(new DeleteCommand(doc, 0, 1), new InsertCommand(doc, 0, "Z"), new DeleteCommand(doc, 10, 1)));
        assertThrows(IndexOutOfBoundsException.class, () -> mgr.execute(bad));
        assertEquals("abc", doc.text());
        mgr.undo();
        assertEquals("", doc.text());                  // only the first insert was in history
    }

    @Test void emptyMacroAndZeroLengthDelete() {
        mgr.execute(new MacroCommand(List.of()));
        mgr.execute(new InsertCommand(doc, 0, "x"));
        mgr.execute(new DeleteCommand(doc, 0, 0));
        mgr.undo();
        assertEquals("x", doc.text());
    }

    @Test void randomOperationsThenFullUndoRestoresEveryStateExactly() {
        Random r = new Random(4);
        List<String> states = new ArrayList<>();
        mgr = new UndoManager(1000);
        states.add(doc.text());
        for (int i = 0; i < 300; i++) {
            if (doc.length() > 0 && r.nextBoolean()) {
                int pos = r.nextInt(doc.length());
                mgr.execute(new DeleteCommand(doc, pos, r.nextInt(doc.length() - pos + 1)));
            } else {
                mgr.execute(new InsertCommand(doc, r.nextInt(doc.length() + 1), "" + (char) ('a' + r.nextInt(26))));
            }
            states.add(doc.text());
        }
        for (int i = states.size() - 2; i >= 0; i--) {
            assertTrue(mgr.undo());
            assertEquals(states.get(i), doc.text());
        }
        for (int i = 1; i < states.size(); i++) {
            assertTrue(mgr.redo());
            assertEquals(states.get(i), doc.text());
        }
    }
}
