package practice.e;

// TODO: validation annotations: author not blank, at most 40 chars; rating 1..5; text not blank, at most 500 chars.
//       (Which one rejects "   "? Which one rejects null? Why is @NotBlank not enough for an int?)
public record CreateReviewRequest(String author, int rating, String text) {}
