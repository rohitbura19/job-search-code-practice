# Answers: E-tariff-estimate-service

(Spoilers. Open only after your attempt.)

Kit note: in `e-spring-tariff-estimate/` the entity is the record `Tariff` (accessors like `ratePerKwh()` instead of getters), `TariffRepository` is a plain interface, `FxClient` takes an injected `RestClient`, and `TariffUpdatedListener.on` has no `@SqsListener`. The reference below is the full version.

**Reference**
```java
@Entity @Table(name = "tariff")
public class Tariff {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 16) private String regionCode;
    @Column(precision = 10, scale = 4) private BigDecimal ratePerKwh;
    private String currency;
    @LastModifiedDate private Instant updatedAt;     // needs @EnableJpaAuditing + @EntityListeners(AuditingEntityListener.class)
}

public interface TariffRepository extends JpaRepository<Tariff, Long> {
    Optional<Tariff> findByRegionCode(String regionCode);
}

public record EstimateRequest(
        @NotBlank String siteId,
        @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal kwh,
        @NotNull @Pattern(regexp = "^[A-Z]{2}(-[A-Z0-9]{1,3})?$") String regionCode,
        @NotNull @Size(min = 3, max = 3) String currency) {}
public record EstimateResponse(String siteId, BigDecimal cost, String currency, boolean fxFallbackUsed) {}

@ConfigurationProperties(prefix = "fx")
@Validated
public record FxProps(@NotBlank String baseUrl, @NotNull Duration timeout) {}
// register with @ConfigurationPropertiesScan on the application class

@Component
public class FxClient {
    private final RestClient http;
    public FxClient(FxProps p) {
        var f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(p.timeout());
        f.setReadTimeout(p.timeout());
        this.http = RestClient.builder().baseUrl(p.baseUrl()).requestFactory(f).build();
    }

    @CircuitBreaker(name = "fx", fallbackMethod = "rateFallback")
    public BigDecimal rate(String from, String to) {
        if (from.equals(to)) return BigDecimal.ONE;
        return http.get().uri("/rates?from={f}&to={t}", from, to)
                   .retrieve().body(FxRate.class).rate();
    }
    // same params + Throwable, same return type
    BigDecimal rateFallback(String from, String to, Throwable t) {
        if (from.equals(to)) return BigDecimal.ONE;
        throw new FxUnavailableException(from, to, t);       // or return last-known rate from a cache
    }
}

@Service                                    // own bean so the @Cacheable proxy is hit (no self-invocation)
public class TariffLookup {
    private final TariffRepository repo;
    public TariffLookup(TariffRepository repo) { this.repo = repo; }

    @Cacheable(cacheNames = "tariffs", key = "#regionCode")
    public Tariff tariff(String regionCode) {
        return repo.findByRegionCode(regionCode).orElseThrow(() -> new NotFoundException(regionCode));
    }

    @CacheEvict(cacheNames = "tariffs", key = "#regionCode")
    public void evict(String regionCode) {}
}

@Service
public class EstimateService {
    private final TariffLookup tariffs;
    private final FxClient fx;
    public EstimateService(TariffLookup tariffs, FxClient fx) { this.tariffs = tariffs; this.fx = fx; }

    @Transactional(readOnly = true)
    public EstimateResponse estimate(EstimateRequest r) {
        Tariff t = tariffs.tariff(r.regionCode());          // goes through the proxy -> cached
        BigDecimal cost = r.kwh().multiply(t.getRatePerKwh());
        if (!t.getCurrency().equals(r.currency())) cost = cost.multiply(fx.rate(t.getCurrency(), r.currency()));
        return new EstimateResponse(r.siteId(), cost.setScale(2, RoundingMode.HALF_UP), r.currency(), false);
    }
}

@Component
public class TariffUpdatedListener {
    private final TariffLookup tariffs;
    public TariffUpdatedListener(TariffLookup tariffs) { this.tariffs = tariffs; }

    @SqsListener("tariff-updated")                                     // io.awspring.cloud.sqs.annotation.SqsListener
    public void on(TariffUpdated msg) { tariffs.evict(msg.regionCode()); }   // exception -> not deleted -> redelivered
}
public record TariffUpdated(String regionCode) {}

@RestController @RequestMapping("/api/v1")
public class EstimateController {
    private final EstimateService service;
    private final TariffLookup tariffs;
    public EstimateController(EstimateService service, TariffLookup tariffs) { this.service = service; this.tariffs = tariffs; }

    @PostMapping("/estimates")
    public EstimateResponse estimate(@Valid @RequestBody EstimateRequest r) { return service.estimate(r); }

    @GetMapping("/tariffs/{regionCode}")
    public Tariff tariff(@PathVariable String regionCode) { return tariffs.tariff(regionCode); }   // 404 via advice
}

@RestControllerAdvice
public class ApiErrors {
    @ExceptionHandler(NotFoundException.class)
    ProblemDetail notFound(NotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
    // MethodArgumentNotValidException -> 400 ProblemDetail is built in once
    // spring.mvc.problemdetails.enabled=true (or extend ResponseEntityExceptionHandler)
}

@WebMvcTest(EstimateController.class)
class EstimateControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean EstimateService service;            // @MockBean before Boot 3.4
    @MockitoBean TariffLookup tariffs;

    @Test void rejectsNegativeKwh() throws Exception {
        mvc.perform(post("/api/v1/estimates").contentType(MediaType.APPLICATION_JSON).content("""
                {"siteId":"s1","regionCode":"IN-KA","kwh":-1,"currency":"INR"}"""))
           .andExpect(status().isBadRequest());
    }
}
```
```yaml
spring:
  cache:
    type: caffeine
    caffeine.spec: maximumSize=500,expireAfterWrite=10m
  mvc.problemdetails.enabled: true
  jpa.open-in-view: false
fx:
  base-url: https://fx.example.com
  timeout: 2s
resilience4j.circuitbreaker.instances.fx:
  sliding-window-size: 20
  failure-rate-threshold: 50
  wait-duration-in-open-state: 30s
management.endpoints.web.exposure.include: health,metrics,prometheus
```
Gotchas:
- **Self-invocation**: if `estimate()` called a `@Cacheable` method on `this`, it would bypass the Spring proxy and the cache would never fire. That is why the cached lookup lives in its own bean (`TariffLookup`) that `EstimateService` injects. In your notepad answer, if you put `tariff()` in the same class, you must say this out loud. Same trap for `@Transactional`, `@Async`, `@CircuitBreaker`. Say this out loud in the interview, it is the classic senior question.
- `FxClient.rate` is also called through an injected bean, so the circuit breaker proxy works.
- Caffeine is a per-pod cache: an SQS message goes to ONE pod only, so the other pods stay stale until TTL. For cross-pod invalidation either use SNS fan-out into one SQS queue per pod (or a pub/sub invalidation topic), or a shared Redis cache, and keep the 10 min TTL as the safety net.
- Money is `BigDecimal` with an explicit scale and `RoundingMode`; `double` cannot represent 0.1 exactly. Construct from strings, never `new BigDecimal(0.1)`.
- `open-in-view: false` stops the JPA session being held open through view rendering, which hides lazy-loading N+1 queries and pins DB connections during slow serialization.
- Open circuit: calls fail fast to the fallback, after `wait-duration` it goes half-open and lets a few trial calls through; success closes it. Add a bulkhead or time limiter so a slow FX service cannot exhaust Tomcat threads.
