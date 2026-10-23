package practice.d20261023.h2;

public sealed interface Entry permits FileEntry, DirEntry {
    String name();

    /** Bytes; for a directory the live sum of everything below it. */
    long size();
}
