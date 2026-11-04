# H2. OOP / core Java: dynamic proxies - memoizing and retrying (20 min)

> Code: `src/main/java/practice/d20261104/h2/Proxies.java` (`@Memo` is given). Test: `ProxiesTest`.

Implement two decorators with `java.lang.reflect.Proxy` - the mechanism behind Spring's JDK-proxy AOP (`@Cacheable`, `@Retryable`, `@Transactional`):

- `memoizing(iface, target)`: results of `@Memo` interface methods are cached per `(method, arguments)`; other methods and `toString/hashCode/equals` pass through; `null` arguments and `null` results work; exceptions are not cached and arrive unchanged; concurrent callers with the same key run the target once.
- `retrying(iface, target, maxAttempts)`: retry on `RuntimeException` only; checked exceptions and errors are rethrown at once, with their own type; the last exception is rethrown after the last attempt.

Pitfalls to find: `InvocationTargetException` and `UndeclaredThrowableException`, `args == null` for no-arg methods, `ConcurrentHashMap` rejects null values, composing the two proxies (memo around retry).
Paper: why does a call from one `@Transactional` method to another in the same class bypass the proxy? When does Spring choose CGLIB instead of a JDK proxy?
