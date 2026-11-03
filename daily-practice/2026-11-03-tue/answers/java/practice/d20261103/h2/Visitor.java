package practice.d20261103.h2;

/** Given: do not edit. */
public interface Visitor<R> {
    R visitFile(FileNode f);
    R visitDir(DirNode d);
}
