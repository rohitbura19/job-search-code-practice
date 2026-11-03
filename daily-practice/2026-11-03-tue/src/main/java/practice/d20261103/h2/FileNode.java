package practice.d20261103.h2;

public final class FileNode implements Node {
    /** name: non-blank, must not contain '/'; size >= 0. Otherwise IllegalArgumentException. */
    public FileNode(String name, long size) { throw new UnsupportedOperationException("TODO"); }

    @Override public String name() { throw new UnsupportedOperationException("TODO"); }
    @Override public long size() { throw new UnsupportedOperationException("TODO"); }
    @Override public <R> R accept(Visitor<R> v) { throw new UnsupportedOperationException("TODO"); }
}
