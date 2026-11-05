# B1. Streams and collections: a library (15 min)

File `b1/LibraryStreams.java` (`Book` is given: title, author, year, pages, tags, nullable rating). Test: `LibraryStreamsTest`.

Implement with streams/collectors: `distinctTags` (case-insensitive distinct, sorted), `titlesByAuthor` (`"A; B"`, ordered by year then title, authors alphabetical), `booksPerDecade`, `pageStats` (`IntSummaryStatistics`), `byRatingDescNullsLast` (ties by title, input untouched), `longestPerAuthor` (`toMap` with a merge function, tie -> smaller title, authors in first-appearance order), `averageRating` (unrated ignored, `OptionalDouble`).

Say out loud: what does `Collectors.toMap` do on a duplicate key without a merge function, and which overload picks the map type?
