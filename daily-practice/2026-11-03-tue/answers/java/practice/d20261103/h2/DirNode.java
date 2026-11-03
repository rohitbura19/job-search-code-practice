package practice.d20261103.h2;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Composite: a directory is a Node that holds Nodes, so clients treat leaf and tree alike (size() recurses).
 * Visitor: double dispatch (node.accept(v) -> v.visitFile/visitDir) adds operations without touching the node classes.
 * A TreeMap keeps children sorted. The parent link enforces "one parent" and lets add() reject cycles by walking up. size() is O(subtree).
 */
public final class DirNode implements Node {
    private final String name;
    private final TreeMap<String, Node> kids = new TreeMap<>();
    private DirNode parent;

    public DirNode(String name) {
        if (name == null || name.isBlank() || name.contains("/")) throw new IllegalArgumentException("bad dir");
        this.name = name;
    }

    @Override public String name() { return name; }

    @Override public long size() {
        long sum = 0;
        for (Node n : kids.values()) sum += n.size();
        return sum;
    }

    public DirNode add(Node child) {
        if (kids.containsKey(child.name())) throw new IllegalArgumentException("duplicate " + child.name());
        if (child instanceof DirNode d) {
            if (d.parent != null) throw new IllegalStateException("already has a parent");
            for (DirNode p = this; p != null; p = p.parent) if (p == d) throw new IllegalStateException("cycle");
            d.parent = this;
        } else if (child instanceof FileNode f) {
            if (f.parent != null) throw new IllegalStateException("already has a parent");
            f.parent = this;
        }
        kids.put(child.name(), child);
        return this;
    }

    public List<Node> children() { return new ArrayList<>(kids.values()); }

    @Override public <R> R accept(Visitor<R> v) { return v.visitDir(this); }
}
