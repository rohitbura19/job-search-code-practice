package practice.d20261103.h2;

/** Leaf of the composite. */
public final class FileNode implements Node {
    private final String name;
    private final long size;
    DirNode parent;

    public FileNode(String name, long size) {
        if (name == null || name.isBlank() || name.contains("/") || size < 0) throw new IllegalArgumentException("bad file");
        this.name = name;
        this.size = size;
    }

    @Override public String name() { return name; }
    @Override public long size() { return size; }
    @Override public <R> R accept(Visitor<R> v) { return v.visitFile(this); }
}
