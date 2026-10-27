> Code: `src/main/java/practice/d20261027/h2/` (`Command` and `TextDocument` are given; write `InsertCommand`, `DeleteCommand`, `MacroCommand`, `UndoManager`). Test: `UndoManagerTest`. **30 min.**

# H2. Command pattern: undo / redo for a text editor (OOP)

- `InsertCommand` / `DeleteCommand` execute against a `TextDocument` and know how to reverse themselves (a delete must remember WHAT it removed).
- `MacroCommand` (Composite): executes parts in order, undoes in reverse, and is all-or-nothing: if part k throws on execute, undo parts 0..k-1 and rethrow.
- `UndoManager(maxHistory)`: `execute` (a failing command leaves history untouched; a new command clears redo; the oldest entry is dropped beyond `maxHistory`), `undo`, `redo`, `canUndo`, `canRedo`.

Say out loud: command objects store the minimal state needed to reverse themselves (vs snapshotting the whole document - memento); why the invoker knows nothing about the receiver; how you would merge consecutive single-character inserts into one undo step.
