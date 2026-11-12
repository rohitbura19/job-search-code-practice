# 20. subscriptions - repository queries

_From daily-practice/2026-11-10-tue. E - Spring Boot: subscriptions API (30 min) - project `e-spring-subscriptions/` (find `TODO(E`)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-10-tue/answers/`.

## Entity `Account`

```java
    private Long id;
    private String email;
    private Instant createdAt;
```

## `AccountRepository`

```java
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    /**
     * TODO(E2e): JPQL: accounts that have NO subscription with status ACTIVE (never subscribed counts too), ordered by email.
     *  Use `not exists` with a correlated subquery over Subscription.
     */
    List<Account> findChurned();
}
```

## Entity `Subscription`

```java
    private Long id;
    private Account account;
    private Plan plan;
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;
    private Instant startedAt;
    private Instant renewsAt;
```

## `SubscriptionRepository`

```java
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // TODO(E2a): derived existence query: does the account (by account id) have a subscription with the given status?
    //            Name it existsByAccountIdAndStatus(Long accountId, SubscriptionStatus status); the repository test calls it.
    /**
     * TODO(E2b): all subscriptions of the account with this e-mail, newest startedAt first (derived query over the nested property account.email),
     *  and load the account in the same SELECT (an entity graph on "account") so reading it later needs no extra query.
     */
    List<Subscription> findByAccountEmailOrderByStartedAtDesc(String email);
    /**
     * TODO(E2c): JPQL returning the PlanCount interface projection: for subscriptions with the given status, `plan` and `count(s)` as `total`,
     *  grouped by plan, ordered by plan. (Plans without rows simply do not appear.)
     */
    List<PlanCount> countByPlan(SubscriptionStatus status);
    /**
     * TODO(E2d): derived query: subscriptions with the given status whose renewsAt lies BETWEEN from and to (both inclusive), earliest renewal first.
     *  Fix the name so Spring Data can derive it (keep the parameter order: status, from, to).
     */
    List<Subscription> findDue(SubscriptionStatus status, Instant from, Instant to);
}
```
