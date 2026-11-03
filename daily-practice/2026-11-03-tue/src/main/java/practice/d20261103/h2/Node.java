package practice.d20261103.h2;

/** Given: do not edit. */
public interface Node {
    String name();
    long size();
    <R> R accept(Visitor<R> v);
}
