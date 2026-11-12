package practice.e;

// TODO(E4): bean validation on the components: rating 1..5; title not blank and at most 80 chars; body optional, at most 2000 chars
public record CreateReview(int rating, String title, String body) {}
