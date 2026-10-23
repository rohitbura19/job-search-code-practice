package practice.d20261023.h2;

import java.util.*;

/**
 * Composite: a directory is an Entry that holds Entries, so size() recurses uniformly. Children live in a TreeMap by name (sorted,
 * O(log n) duplicate check). The cycle check asks "does the added directory already contain me?". children() returns a copy;
 * paths() and largestFile() share one private walk that builds a path -> file TreeMap.
 */
public final class DirEntry implements Entry {
    private final String name;
    private final TreeMap<String, Entry> children = new TreeMap<>();

    public DirEntry(String name) {
        if (name == null || name.isBlank() || name.indexOf('/') >= 0) throw new IllegalArgumentException("bad name: " + name);
        this.name = name;
    }

    @Override public String name() { return name; }

    @Override public long size() {
        long sum = 0;
        for (Entry e : children.values()) sum += e.size();
        return sum;
    }

    public DirEntry add(Entry child) {
        Objects.requireNonNull(child, "child");
        if (children.containsKey(child.name())) throw new IllegalArgumentException("duplicate name: " + child.name());
        if (child instanceof DirEntry d && (d == this || d.contains(this))) throw new IllegalArgumentException("cycle");
        children.put(child.name(), child);
        return this;
    }

    private boolean contains(DirEntry target) {
        for (Entry e : children.values()) if (e == target || (e instanceof DirEntry d && d.contains(target))) return true;
        return false;
    }

    public List<Entry> children() { return List.copyOf(children.values()); }

    public List<String> paths() { return List.copyOf(walk().keySet()); }

    public Optional<FileEntry> largestFile() {
        FileEntry best = null;
        for (FileEntry f : walk().values()) if (best == null || f.size() > best.size()) best = f;   // path order => first max wins ties
        return Optional.ofNullable(best);
    }

    private TreeMap<String, FileEntry> walk() {
        TreeMap<String, FileEntry> out = new TreeMap<>();
        walk("", out);
        return out;
    }

    private void walk(String prefix, Map<String, FileEntry> out) {
        for (Entry e : children.values()) {
            if (e instanceof FileEntry f) out.put(prefix + f.name(), f);
            else ((DirEntry) e).walk(prefix + e.name() + "/", out);
        }
    }
}
