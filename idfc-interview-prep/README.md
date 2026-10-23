# IDFC FIRST Bank – Interview Question Set

These are 15 problem statements, written in the same style as IDFC's printed assessment sheet. Each one is based on a question IDFC has actually asked: in Rohit's own round, on the shared sample sheet, or in candidates' write-ups on LeetCode, Medium and AmbitionBox.

## How the IDFC coding round works

- It's in person, usually on your own laptop. The sheet says **25 minutes, Java**.
- You need **correct output** to advance. A good explanation alone was not enough for at least one candidate.
- After coding, you explain your logic, and they ask about edge cases.
- The evaluation form scores four things:
  - **Functionality**: you produce the expected output.
  - **Approach**: logical problem solving, good data structures, and splitting the problem into parts.
  - **Code quality**: readability and structure.
  - **Efficiency**: time and space complexity, and no redundant steps.
- The final decision is one of Select / Reject / Needs Further Evaluation.

## How to practise

1. Start a 25-minute timer and read only the question file.
2. Write OOP code: a model class or record, an enum where it fits, a service class, and a `main` that prints the examples.
3. Run every example given and check each line of output matches.
4. Out loud, in 3 minutes: explain your approach, data structures, complexity, edge cases, and the first follow-up.

## Questions

| # | Question | Type | Where it was seen | Time |
|---|---|---|---|---|
| 01 | [Car on an XY plane](questions/01-car-xy-plane.md) | OOP coding | Rohit's round | 25 min |
| 02 | [Library Fine Summarizer](questions/02-library-fine-summarizer.md) | OOP + maps | Sample sheet, Medium | 25 min |
| 03 | [Card Discount Calculation](questions/03-card-discount-calculation.md) | Rules engine | Sample sheet | 25 min |
| 04 | [Highest Value Card](questions/04-highest-value-card.md) | Parsing + comparison | Sample sheet, LeetCode x2 | 15 min |
| 05 | [Lift `move()`](questions/05-lift-move.md) | OOP / LLD | LeetCode | 30 min |
| 06 | [Log storage with 1-year retention](questions/06-log-retention-store.md) | LLD + design | LeetCode | 30 min |
| 07 | [University Academic Tracking System](questions/07-university-academic-tracking.md) | LLD with follow-ups | Medium (AVP round) | 40 min |
| 08 | [Apartment notice ticker board](questions/08-apartment-ticker-board.md) | LLD | LeetCode | 30 min |
| 09 | [Mini Instagram](questions/09-mini-instagram.md) | LLD + schema + API | LeetCode | 40 min |
| 10 | [Implement a Max Heap](questions/10-max-heap.md) | DS from scratch | LeetCode | 20 min |
| 11 | [Find All People With Secret](questions/11-people-with-secret.md) | Graph / DSU | LeetCode | 30 min |
| 12 | [First 1 in a sorted 0/1 array](questions/12-first-one-binary-search.md) | Binary search | LeetCode | 15 min |
| 13 | [Banking SQL: JOIN + GROUP BY](questions/13-sql-joins-group-by.md) | SQL | LeetCode | 25 min |
| 14 | [ECS on EC2 vs Fargate](questions/14-ecs-ec2-vs-fargate.md) | AWS concept | LeetCode | 10 min |
| 15 | [Implement methods in a given class (Account)](questions/15-implement-given-class-account.md) | Online assessment style | LeetCode (OA) | 30 min |

Some notes on the questions:

- The sample sheet's rules for Q3 contradict its expected output. The question file states the only reading of the rules that gives 130 for 100 and 50.
- The examples in every question were checked with a small script.
