package practice.e;

public record CommentView(Long id, String author, String text) {
    static CommentView from(Comment c) { return new CommentView(c.getId(), c.getAuthor(), c.getText()); }
}
