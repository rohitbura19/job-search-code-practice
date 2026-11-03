package practice.d20261103.h2;

import java.util.List;

/** Implement each operation as a Visitor: no instanceof checks and no new methods on the node classes. */
public final class Visitors {
    private Visitors() {}

    /** Number of files below the node (a file itself counts 1; empty directories count 0). */
    public static Visitor<Integer> fileCount() { throw new UnsupportedOperationException("TODO"); }

    /** Paths of all files whose name ends with the suffix, starting with the visited node's own name, e.g. "root/src/A.java"; children in name order. */
    public static Visitor<List<String>> paths(String suffix) { throw new UnsupportedOperationException("TODO"); }

    /** Text tree: one line per node, 2 spaces of indent per depth, directories end with "/", files show " (size)"; children in name order, lines joined with "\n", no trailing newline. */
    public static Visitor<String> render() { throw new UnsupportedOperationException("TODO"); }
}
