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

## Days

<!-- The routine appends one line per day below. -->
