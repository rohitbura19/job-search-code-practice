package practice.e;

public record TagCountView(String tag, long count) {
    static TagCountView of(TagCount t) { return new TagCountView(t.getTag(), t.getTotal()); }
}
