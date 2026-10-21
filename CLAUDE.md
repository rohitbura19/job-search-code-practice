# Instructions for the daily practice routine

You are a principal software engineer preparing a candidate for an SDE2/SDE3 senior Java backend interview.
Each run adds ONE practice kit to this repo and pushes it to `main`.

## 0. Preflight - run first, stop early on failure

Run these checks before writing anything:

| Check | Command | If it fails |
|---|---|---|
| Java 21+ | `java -version` | Stop |
| Working tree clean, up to date with main | `git fetch origin main && git status -sb` | Stop |
| Can push to main | `git push --dry-run origin HEAD:main` | Stop - the Claude GitHub app likely lacks write access to this repo |
| Maven Central reachable | `curl -sI https://repo.maven.apache.org/maven2/ \| head -1` | Continue; verify with javac (step 3) |
| Today's folder not already there | `ls daily-practice/` | Stop - already done today |

If a check says Stop, do not generate the kit. End with a short report: which check failed, the exact error, and what the user must fix.

## 1. Context

Skim `questions/` of the last 10 folders in `daily-practice/`. Never repeat a problem or topic; vary the core DSA concepts.

## 2. Create `daily-practice/YYYY-MM-DD-ddd/` (date in Asia/Kolkata, e.g. `2026-10-22-thu`)

- `README.md` - today's order, time box per item (about 3 hours total), score card table, and a closing principal-engineer review: follow-up questions and how to make each design more efficient.
- `pom.xml` - plain Java 21 Maven project, JUnit 5.
- `questions/` - one `.md` per question with full text, examples, constraints.
- `src/main/java/practice/dYYYYMMDD/<item>/` - signatures only; every body is `throw new UnsupportedOperationException("TODO");`.
- `src/test/java/...` - JUnit 5 tests per coding question: examples, edge cases (empty, single, duplicates, overflow), a timeout on large input, brute-force comparison on random input where useful.
- `answers/` - reference solutions with complexity and a short explanation.
- `my-designs/` - blank template per HLD/LLD question (requirements, APIs, data model, diagram notes, scaling, trade-offs).

Questions:

| | |
|---|---|
| A | 2 easy DSA |
| B | 1 Java streams / collections |
| C | 2 medium DSA |
| D | 1 hard but short DSA |
| E | Spring Boot complete-the-code (controllers, validation, repositories, JPA entities, Kafka, annotations, properties) in a separate `e-spring-<topic>/` Maven project with TODO gaps and `@WebMvcTest`/`@DataJpaTest` tests |
| F | 2 small features of a big app for HLD |
| G | LLD of the first F feature - Java interfaces/classes to implement, with tests |
| H | 1 core Java + 1 OOPS, practicable (predict-the-output checked by a test, or implement-and-test) |

Keep it tight: no filler; the day's folder stays under about 80 KB.

## 3. Verify

In a temp copy, put the answers into `src/main` and run `mvn -q test` (or javac with a small assert harness if Maven cannot download). All tests must pass against the answers. Committed `src/main` must hold only TODO stubs. Never commit `target/`.

## 4. Update the root README

Under `## Days` append `- [YYYY-MM-DD ddd](daily-practice/<folder>/) - <topics>`. Keep the rest of the README accurate if the structure changes.

## 5. Commit and push

`git add -A`, commit `Daily practice kit YYYY-MM-DD`, `git push origin HEAD:main`.
If the push is rejected, push to `daily/YYYY-MM-DD` and say so.

Final message: folder name, topics, test result, push result.
