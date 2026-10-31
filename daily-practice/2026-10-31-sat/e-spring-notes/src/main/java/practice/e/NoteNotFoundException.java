package practice.e;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(long id) { super("note " + id + " not found"); }
}
