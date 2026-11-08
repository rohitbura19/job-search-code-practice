# H2 - Command pattern: editor with undo/redo (OOP, 20 min) - `h2/`

Implement `InsertCommand`, `DeleteCommand` (remembers the removed text), `MacroCommand` (composite, atomic execute with compensating undo, copies its list) and `Editor` (bounded undo history, redo cleared by a new command, a failing command is not recorded). Javadoc on each class has the exact rules; `EditorTest` checks them, including a random undo-everything property test.
