> Code: `src/main/java/practice/d20261014/h2/`. Part 1: read `H2Snippets.java`, fill `H2Predictions.java`. Part 2 (item 5): implement `LoggingNotifier`, `RetryingNotifier`, `MetricNotifier`. Test: `H2PredictionsTest`, `NotifierDecoratorTest`.

### H2. OOPS: initialization order and calling overridable methods in a constructor
```java
class P {
    static { System.out.println("P-static"); }
    { System.out.println("P-inst"); }
    P() { System.out.println("P-ctor"); show(); }
    void show() { System.out.println("P-show"); }
}
class C extends P {
    static { System.out.println("C-static"); }
    int v = 7;
    { System.out.println("C-inst"); }
    C() { super(); System.out.println("C-ctor v=" + v); }
    @Override void show() { System.out.println("C-show v=" + v); }
}
new C();
```
1. Write the exact output of `new C()` and explain the order. (In the snippets each line is joined with `|`; `firstInstance()` is the first `new C()` in the JVM, `secondInstance()` the next one.)
2. Why is `v` printed as `0` inside `C-show`? Name the rule this violates and fix it three ways.
3. Static blocks: when exactly do they run (class initialization, not `new`)? Give two ways a class gets initialized.
4. An `abstract` class constructor calls an abstract method, and a Spring `@Component` subclass relies on an injected field inside it. What breaks?
5. Inheritance or composition: your `NotifierBase` has `send()`; now you need logging, retry and metrics around it. Show why a Decorator beats a class hierarchy `LoggingRetryingMetricNotifier extends ...`. (Code it: the three decorators, each implementing `Notifier`.)
