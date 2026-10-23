package practice.d20261023.h2;

import java.util.List;
import java.util.Optional;

/**
 * A directory in a composite tree. Rules (each is tested):
 * - name: not null, not blank, no '/'  -> IllegalArgumentException.
 * - add(child): returns this (fluent). null -> NullPointerException. A name already used by a direct child -> IllegalArgumentException.
 *   Adding this directory to itself, or a directory that (transitively) contains this one, would create a cycle -> IllegalArgumentException.
 * - size() is live: it reflects later changes made to nested directories.
 * - children() is an unmodifiable snapshot sorted by name; callers cannot corrupt the tree through it.
 * - paths(): relative path of every FILE below, '/'-separated, sorted ("sub/y.txt", "x.txt"); empty directories add nothing.
 * - largestFile(): the biggest file anywhere below; ties go to the smaller path; empty when there is no file.
 */
public final class DirEntry implements Entry {
    public DirEntry(String name) {
        // TODO
    }

    @Override public String name() { throw new UnsupportedOperationException("TODO"); }
    @Override public long size() { throw new UnsupportedOperationException("TODO"); }
    public DirEntry add(Entry child) { throw new UnsupportedOperationException("TODO"); }
    public List<Entry> children() { throw new UnsupportedOperationException("TODO"); }
    public List<String> paths() { throw new UnsupportedOperationException("TODO"); }
    public Optional<FileEntry> largestFile() { throw new UnsupportedOperationException("TODO"); }
}
