# SQL / JPQL quick practice

Every query question from the daily kits and the IDFC prep. Questions only; answers stay in `daily-practice/<day>/answers/`.

## `sql/` - plain SQL

| File | What |
|---|---|
| [01-banking](sql/01-banking/) | `schema.sql` (tables + data) and `questions.sql`: 6 queries - LEFT JOIN + COALESCE, WHERE vs HAVING, GROUP BY, NOT EXISTS, RANK() OVER, negative balance. Expected results are under each question. |
| [02-outbox-skip-locked.sql](sql/02-outbox-skip-locked.sql) | Native `FOR UPDATE SKIP LOCKED` batch + bulk mark-as-sent |

Run `schema.sql` in any database (PostgreSQL, MySQL 8, H2, SQLite, or an online SQL playground), then write each query in `questions.sql`.

## `jpql/` - Spring Data repository queries

One file per day: the entity fields, then the repository with TODO comments. Write the `@Query` / `@Modifying` / `@Lock` / `@EntityGraph` or derived-method name for each.

| # | Topic | Practises |
|---|---|---|
| 01 | meter-readings | derived `Between`, `coalesce(sum)` |
| 02 | slot-booking | atomic conditional `update ... where booked < capacity` |
| 03 | catalog | derived paged query, interface projection `group by`, soft delete |
| 04 | coupons | atomic redeem update |
| 05 | refunds | derived + paged, enum literal in JPQL |
| 06 | devices | `ContainingIgnoreCase`, embedded property, bulk update |
| 07 | orders | `@EntityGraph`, `In`, constructor expression + `sum` |
| 08 | reviews | constructor expression `count`/`avg`, bulk status update |
| 09 | inventory | check-and-reserve update, `coalesce(sum)` |
| 10 | notes | case-insensitive `like` search, native update |
| 11 | reading-list | join over element collection, tag counts |
| 12 | wallet | `@Lock(PESSIMISTIC_WRITE)` select for update |
| 13 | bookings | overlap test, `countQuery`, bulk delete |
| 14 | tickets | optional filters + `Slice`, group-by projection, bulk update |
| 15 | shipments | optional filter + `countQuery` |
| 16-22 | profiles, tasks, courses, invoices, subscriptions, notifications, reviews | specifications, fetch join, `size()`, `not exists`, `type()` over inheritance, `avg` |
