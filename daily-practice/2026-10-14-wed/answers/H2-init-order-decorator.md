# Answers: H2-init-order-decorator

(Spoilers. Open only after your attempt.)

**Reference** (output verified by running the code)
- Output, in order: `P-static`, `C-static`, `P-inst`, `P-ctor`, `C-show v=0`, `C-inst`, `C-ctor v=7`. Class initialization first (superclass static, then subclass static, once per class load), then for the instance: superclass instance initializers and fields, superclass constructor body, subclass instance initializers and field initializers, subclass constructor body. Because `P`'s constructor runs BEFORE `C`'s field initializers, the virtual call `show()` dispatches to `C.show` while `v` still holds its default `0`.
- Rule violated: never call an overridable method from a constructor (Effective Java; the subclass runs against a half-constructed object). Fixes: make the method `final` or `private`; do the work in a static factory (`create()`) after construction; or use lazy initialization / an explicit `init()` called by the factory. Constructors should only assign fields.
- Static blocks run at class initialization: first `new`, first static method call, first non-constant static field access, `Class.forName(name)` (with initialize = true), or initialization of a subclass. Not on merely referencing a `static final` compile-time constant or on `Foo.class`.
- Spring: the constructor of the abstract parent runs before field or setter injection of the subclass, so an `@Autowired` field is still `null` there (use constructor injection or `@PostConstruct` instead).
- Decorator: `LoggingNotifier(Notifier inner)`, `RetryingNotifier(Notifier inner, ...)`, `MetricNotifier(Notifier inner)`, each implementing `Notifier`; compose `new Metric(new Retry(new Logging(real)))` in any order, add or drop a behaviour without a new class per combination. Inheritance gives 2^n subclasses for n optional behaviours, couples you to the parent's internals, and cannot be rearranged at runtime. This is also what Spring AOP proxies and `@Retryable`/`@Timed` do under the hood.
- A second `new C()` in the same JVM prints the same lines without `P-static` and `C-static`.
