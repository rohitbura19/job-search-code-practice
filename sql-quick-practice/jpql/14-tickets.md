# 14. tickets - repository queries

_From daily-practice/2026-11-04-wed. E. Spring Boot: support tickets API (30 min)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-04-wed/answers/`.

## Entity `Ticket`

```java
@Entity
@Table(name = "tickets")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 100) private String title;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Status status = Status.OPEN;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Priority priority;
    @Column(length = 80) private String assignee;
    @Column(nullable = false) private Instant createdAt;
    @Version private long version;
```

## `TicketRepository`

```java
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // TODO: optional filters, newest first, as a Slice (no count query). A null status / null assignee means "do not filter on it".
    //  JPQL @Query with @Param, e.g.  (:status is null or t.status = :status)  and  (:assignee is null or t.assignee = :assignee),
    //  order by t.createdAt desc, t.id desc (the id makes the order stable for equal timestamps).
    Slice<Ticket> search(Status status, String assignee, Pageable pageable);
    // TODO: how many tickets per status: JPQL "select t.status as status, count(t) as total ... group by t.status", mapped to the StatusCount projection.
    List<StatusCount> countByStatus();
    // TODO: bulk update. Every OPEN ticket created before `cutoff` whose priority is not yet URGENT becomes URGENT; return the number of rows changed.
    //  Needs @Modifying and @Query. Make the persistence context forget stale entities after the update (clearAutomatically).
    //  (A bulk JPQL update does not touch @Version and fires no entity callbacks - say why that matters.)
    int escalateStale(Instant cutoff);
}
```
