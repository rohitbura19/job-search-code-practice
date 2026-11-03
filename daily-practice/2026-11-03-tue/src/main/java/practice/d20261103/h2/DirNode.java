package practice.d20261103.h2;

import java.util.List;

public final class DirNode implements Node {
    /** name: same rule as FileNode. */
    public DirNode(String name) { throw new UnsupportedOperationException("TODO"); }

    @Override public String name() { throw new UnsupportedOperationException("TODO"); }

    /** Sum of the sizes of everything below. Must stay correct after later add() calls (no stale cached value). */
    @Override public long size() { throw new UnsupportedOperationException("TODO"); }

    /**
     * Adds a child and returns this (fluent). IllegalArgumentException if a child with the same name exists;
     * IllegalStateException if the node already belongs to a directory (a node has one parent) or if adding would create a cycle (child == this or an ancestor).
     */
    public DirNode add(Node child) { throw new UnsupportedOperationException("TODO"); }

    /** Children sorted by name (a copy). */
    public List<Node> children() { throw new UnsupportedOperationException("TODO"); }

    @Override public <R> R accept(Visitor<R> v) { throw new UnsupportedOperationException("TODO"); }
}
