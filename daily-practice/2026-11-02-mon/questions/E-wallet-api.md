# E. Spring Boot: wallet API with idempotent, deadlock-free transfers (30 min)

> Project: `e-spring-wallet/` (own `pom.xml`; `cd e-spring-wallet && mvn test`). Tests: `AccountRepositoryTest` (`@DataJpaTest`), `WalletControllerTest` (`@WebMvcTest`), `WalletServiceTest` (Mockito + fixed `Clock`), `WalletPropsTest` (`ApplicationContextRunner`). **30 min.**

Complete the `TODO` gaps (entities, views, exceptions, `TransferRepository` and the application class are given):

1. `AccountRepository.findByIdForUpdate`: `@Lock(PESSIMISTIC_WRITE)` plus a JPQL `@Query`.
2. `AccountRequest`, `TransferRequest`: Bean Validation (not blank, size, `@DecimalMin`, `@Digits`, `@PositiveOrZero`).
3. `WalletProps`: `@ConfigurationProperties("wallet")` record with `@Validated` and `@DefaultValue`.
4. `WalletService`: `@Service`, `@Transactional`; replay by Idempotency-Key, same-key-different-payload conflict, ascending-id locking, balance check.
5. `WalletController`: `201 + Location`, `201/200` for new/replayed transfer, validated `Idempotency-Key` header.
6. `ApiExceptionHandler`: `ProblemDetail` mapping (404, 409, 422); validation stays 400.

Questions on paper: why lock in id order? What does `@Version` add on top of the pessimistic lock? Why can two concurrent requests with the same key still both pass the service check, and what stops the second one? Why is the replay `200` and not `201`?
