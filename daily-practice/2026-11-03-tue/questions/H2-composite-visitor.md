# H2. OOP: Composite + Visitor, a file tree (20 min)

> Code: `src/main/java/practice/d20261103/h2/` (`FileNode`, `DirNode`, `Visitors`; `Node` and `Visitor` are given). Test: `FileSystemTest`.

- `FileNode(name, size)` and `DirNode(name)`: names non-blank without `/`; `DirNode.size()` is the live sum of its children.
- `DirNode.add(child)` is fluent; duplicate names are rejected; a node has one parent and a directory can never contain itself or an ancestor; `children()` is a sorted copy.
- Implement three operations as visitors, without `instanceof` and without adding methods to the nodes: `fileCount()`, `paths(suffix)`, `render()`.

Paper: what does the Visitor buy you over a `describe()` method on each node, and what does it cost when you add a new node type?
