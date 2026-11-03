package practice.d20261014.c2;

/** C2 - union-find over nodes 1..n (index 0 unused). Iterative find with path compression, union by rank. */
public final class Dsu {
    /** n nodes labelled 1..n, each in its own set. */
    public Dsu(int n) { throw new UnsupportedOperationException("TODO"); }

    public int find(int x) { throw new UnsupportedOperationException("TODO"); }

    /** Returns false if a and b were already connected (this edge closes a cycle). */
    public boolean union(int a, int b) { throw new UnsupportedOperationException("TODO"); }

    /** Number of connected components right now (starts at n, drops on every successful union). */
    public int count() { throw new UnsupportedOperationException("TODO"); }
}
