# job-search-code-practice

Daily interview practice kits for a senior Java backend role (SDE2 / SDE3).
A scheduled Claude cloud routine adds one new folder every morning (9:00 AM IST),
and a task on my PC pulls it into `C:\Users\Rohit Bura\Desktop\Code\job-search-code-practice`.

## What each day contains

```
daily-practice/
  2026-10-22-thu/
    README.md          today's order, time boxes and score card
    pom.xml            plain-Java Maven project (JUnit 5)
    questions/         one .md file per question, full text
    src/main/java/     empty classes - method signatures throw "TODO"
    src/test/java/     JUnit tests for every coding question
    answers/           reference solutions - open only after trying
    my-designs/        blank templates for the HLD / LLD questions
    e-spring-*/        separate Spring Boot project for the fill-in-the-code question
```

Sections in every sheet:

| Section | What |
|---|---|
| A | 2 easy DSA questions |
| B | 1 Java streams / collections question |
| C | 2 medium DSA questions |
| D | 1 hard (but short) DSA question |
| E | Spring Boot fill-in-the-code (controllers, repositories, Kafka, annotations, properties) |
| F | 2 HLD questions - small features of a big app |
| G | LLD of the same feature |
| H | 1 core Java + 1 OOPS question |

Each sheet ends with a principal-engineer review: how to change the design to make it more efficient.

## How to practise

1. Open the day's folder in VS Code (Extension Pack for Java).
2. Read `questions/`, write code in `src/main/java/`, and run the tests:
   `mvn test` or `mvn test -Dtest=ClassNameTest`.
3. Compare with `answers/` and fill in the score card in the day's README.

## Company prep

- [IDFC FIRST Bank](idfc-interview-prep/) - 15 IDFC-style problem statements (car on XY plane, library fines, card discount, highest card, lift move(), log retention, academic tracker, ticker board, mini Instagram, max heap, people with secret, first 1 binary search, banking SQL, ECS vs Fargate, Account class)

## Days

<!-- The routine appends one line per day below. -->
- [2026-10-11 sun](daily-practice/2026-10-11-sun/) - Contains Duplicate II, Valid Parentheses, Collectors, Merge Intervals + meeting rooms, Course Schedule II (Kahn), Sliding Window Maximum, Spring Boot + Kafka idempotent listener, notification service HLD/LLD, ConcurrentHashMap, Liskov/composition
- [2026-10-12 mon](daily-practice/2026-10-12-mon/) - Reverse linked list, binary search (lower bound, on the answer), Collectors, islands + union-find, coin change DP, two-heap median, rate limiter HLD/LLD, live session push HLD, ThreadPoolExecutor, overloading and singletons
- [2026-10-13 tue](daily-practice/2026-10-13-tue/) - Tree diameter, stock profit (greedy/cooldown), Collectors + LRU, longest substring window, Combination Sum, Trapping Rain Water, Spring cache/RestClient/validation, OTA rollout HLD/LLD, try/finally + String pool, immutability + DIP
- [2026-10-21 wed](daily-practice/2026-10-21-wed/) - Add Binary + Excel column, Pascal, streams stats/collections views, Quickselect, build tree from traversals, weighted job scheduling, Spring validation + ProblemDetail + ETag, nearby chargers HLD/LLD (geo index), offline sync HLD, lambdas, enum state machine
- [2026-10-22 thu](daily-practice/2026-10-22-thu/) - Move Zeroes + Remove Duplicates II, Valid Parentheses + min insertions, collectors (groupingBy/partitioningBy/teeing), Course Schedule II (Kahn), Coin Change I/II, Sliding Window Maximum, Spring JPA + validation + ProblemDetail (meter readings), notification guard HLD/LLD (sliding-window limiter), wallet transfer HLD, core Java predictions, immutable Money (OOP)
- [2026-10-23 fri](daily-practice/2026-10-23-fri/) - Majority Element I/II (Boyer-Moore), House Robber I/II, collectors (groupingBy/teeing/partitioningBy), Top K Frequent Words, LIS + Russian Doll, Largest Rectangle in Histogram, Spring JPA atomic update + service + ProblemDetail (slot booking), delayed job scheduler HLD/LLD, leaderboard HLD, core Java predictions, composite directory tree (OOP)
- [2026-10-24 sat](daily-practice/2026-10-24-sat/) - Valid Palindrome I/II, linked-list cycle (Floyd), collectors + NavigableMap ranges, rotated-array search/min, rotting oranges + matrix BFS, Minimum Window Substring, Spring paging + projection + @ConfigurationProperties + ProblemDetail (catalog), feature-flag service HLD/LLD (sticky bucketing), webhook delivery HLD, core Java predictions, sealed Expr + pattern matching (OOP)
- [2026-10-25 sun](daily-practice/2026-10-25-sun/) - Intersection of Two Arrays II, Min Stack, collectors on bookings, prefix products + subarray sum = k, Accounts Merge (union-find), Median of Two Sorted Arrays, Spring atomic update + Clock + validation + ProblemDetail (coupons), URL shortener HLD/LLD (Base62, atomic claim), presence HLD, core Java predictions, Decorator pricing (OOP)
