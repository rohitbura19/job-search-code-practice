> Practice: read `h1/H1Snippets.java`, type your predicted outputs into `h1/H1Predictions.java`, run `H1PredictionsTest`. Line 9 (HotSpot detail) and line 11 (compile error) are paper-only.

### H1. Core Java: lambdas, method references, `Optional` and functional-interface puzzlers
```java
List<Runnable> rs = new ArrayList<>();
for (int i = 0; i < 3; i++) { int j = i; rs.add(() -> print(j + " ")); }
for (String s : List.of("a", "b")) rs.add(() -> print(s + " "));
rs.forEach(Runnable::run);                                                                          // line 1
// line 1b: replace the first loop body by   rs.add(() -> print(i + " "));   -> ?
int[] c = {0};  Runnable inc = () -> c[0]++;  inc.run(); inc.run();  print(c[0] + "");              // line 2

static class Greeter { String name = "outer";
    Runnable lam()  { return () -> print(this.name + " " + getClass().getSimpleName()); }
    Runnable anon() { return new Runnable() { String name = "anon"; public void run() { print(this.name + " " + getClass().getSimpleName().isEmpty()); } }; } }
new Greeter().lam().run();  new Greeter().anon().run();                                             // line 3

String s = null;  Supplier<String> late = () -> s.toString();   // compiles
Supplier<String> early = s::toString;                                                               // line 4: which one throws, and when?

StringBuilder[] holder = {new StringBuilder("first")};
Supplier<String> viaRef = holder[0]::toString;  Supplier<String> viaLam = () -> holder[0].toString();
holder[0] = new StringBuilder("second");  print(viaRef.get() + " " + viaLam.get());                  // line 5

static String expensive() { print("expensive "); return "x"; }
Optional.of("v").orElse(expensive());  Optional.of("v").orElseGet(() -> expensive());
print(Optional.of(1).map(i -> (String) null).isPresent() + "");   // Optional.of(null)  ?   Optional.empty().get()  ?         // line 6

Function<Integer,Integer> f = x -> x + 1, g = x -> x * 2;   print(f.andThen(g).apply(5) + " " + f.compose(g).apply(5));    // line 7

static void run(Runnable r) { print("Runnable "); }   static <T> void run(Callable<T> c) { print("Callable "); }
run(() -> { throw new RuntimeException(); });  run(() -> System.out.println());  run(() -> "x");  run(() -> { });  run(() -> five());   // line 8, five() returns int

static Runnable nonCapturing() { return () -> {}; }   static Runnable capturing(int i) { return () -> print("" + i); }
print((nonCapturing() == nonCapturing()) + " " + (capturing(1) == capturing(1)));                   // line 9

ExecutorService ex = Executors.newSingleThreadExecutor();
Future<?> fu = ex.submit(() -> { throw new IllegalStateException("boom"); });   // line 10: where does "boom" go? and for ex.execute(...)?
// line 11: Comparator<String> c = Comparator.comparing(s -> s.length()).reversed();    does it compile? and with String::length?  with (String s) -> s.length() ?
```
1. Predict every line, with the reason. Which lines are decided at compile time and which at run time?
2. What does "effectively final" mean, why does Java require it for captured locals, and what are the two workarounds (a one-element array or an `AtomicInteger`; a field)? Which workaround is wrong in a parallel stream?
3. How does `this` differ between a lambda and an anonymous class? Is a lambda an inner class (bytecode: `invokedynamic`, `LambdaMetafactory`)? What does that mean for `getClass().getSimpleName()`, identity and serialisation?
4. List the four kinds of method reference (static, bound instance `obj::m`, unbound instance `Type::m`, constructor `Type::new`) and say when the receiver of a bound reference is evaluated.
5. `orElse` versus `orElseGet`; why is `Optional` for return types only (not fields or parameters)? `map` versus `flatMap` on `Optional`; the three ways to unwrap safely (`orElseThrow`, `orElse`, `ifPresentOrElse`).
6. Overload resolution with a lambda argument: `Runnable` versus `Callable`. Why does `executor.submit(() -> { throw ... })` pick `Callable`, and what happens to an exception thrown in a task given to `execute` versus `submit`?
7. Why does `Comparator.comparing(s -> s.length()).reversed()` fail to compile? (Inference: the lambda has no target type once `.reversed()` is chained.) Name the three fixes.
