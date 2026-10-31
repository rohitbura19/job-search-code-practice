# B1. Streams / collectors on movie ratings (15 min) - `b1/MovieStats.java`

`record Rating(String user, String movie, String genre, int stars)`. Implement with streams and collectors (no manual loops over the ratings except `histogram`'s seeding):
1. `avgByGenre` - genre -> average stars, keys sorted.
2. `likedMoviesByUser` - every user -> sorted set of movies rated >= 4. A user with no such movie keeps an empty set (hint: `Collectors.filtering`, not `Stream.filter`).
3. `histogram` - stars 1..5 -> count, all five keys, zero when absent.
4. `topMovie(ratings, minVotes)` - highest average among movies with at least `minVotes` ratings; ties: more votes, then name. `summarizingInt` helps.
5. `bestAndWorstGenre` - `"best=<g> worst=<g>"` by average (ties: name ascending), `"none"` when empty, with `Collectors.teeing`.
6. `genreExplorers(ratings, n)` - users with ratings in >= n distinct genres, sorted.

Paper: (a) `filtering` vs `filter` before `groupingBy`; (b) what does `teeing` buy over two passes; (c) why is `averagingInt` on an empty group never reached; (d) `Collectors.toMap` vs `groupingBy` when keys repeat.
