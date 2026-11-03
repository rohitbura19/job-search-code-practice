package practice.d20261103.h2;

import java.util.ArrayList;
import java.util.List;

public final class Visitors {
    private Visitors() {}

    public static Visitor<Integer> fileCount() {
        return new Visitor<>() {
            public Integer visitFile(FileNode f) { return 1; }
            public Integer visitDir(DirNode d) {
                int n = 0;
                for (Node c : d.children()) n += c.accept(this);
                return n;
            }
        };
    }

    public static Visitor<List<String>> paths(String suffix) {
        return new Visitor<>() {
            public List<String> visitFile(FileNode f) { return f.name().endsWith(suffix) ? List.of(f.name()) : List.of(); }
            public List<String> visitDir(DirNode d) {
                List<String> out = new ArrayList<>();
                for (Node c : d.children()) for (String p : c.accept(this)) out.add(d.name() + "/" + p);
                return out;
            }
        };
    }

    public static Visitor<String> render() {
        return new Visitor<>() {
            private int depth;
            public String visitFile(FileNode f) { return "  ".repeat(depth) + f.name() + " (" + f.size() + ")"; }
            public String visitDir(DirNode d) {
                StringBuilder sb = new StringBuilder("  ".repeat(depth)).append(d.name()).append('/');
                depth++;
                for (Node c : d.children()) sb.append('\n').append(c.accept(this));
                depth--;
                return sb.toString();
            }
        };
    }
}
