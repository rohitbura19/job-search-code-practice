> Code: predict the puzzle in `h2/H2Predictions.java` (read `H2Snippets.java`; test `H2PredictionsTest`; line 4 adds the static/field hiding of question 2), write the three Singletons in `h2/EnumSingleton.java`, `HolderSingleton.java`, `DclSingleton.java` (test `SingletonTest`) and the Open/Closed payment dispatcher in `h2/PaymentProcessor.java` (test `PaymentProcessorTest`).

### H2. OOPS: Overloading vs overriding, and Singleton
```java
class A {
    String who(Object o) { return "A-obj"; }
    String who(String s) { return "A-str"; }
}
class B extends A {
    @Override String who(Object o) { return "B-obj"; }
    String who(Integer i) { return "B-int"; }
}
A a = new B();
Object o = "hi";
System.out.println(a.who(o));
System.out.println(a.who("hi"));
System.out.println(a.who(5));
```
1. What does each line print and why?
2. Static vs dynamic dispatch: what is decided at compile time and what at runtime? What happens with `static` methods and fields (hiding)?
3. Write a thread-safe Singleton three ways. Which is best and why? Is a Spring `@Component` singleton the same thing?
4. Which SOLID principle would you point at when a new payment type forces edits to a long `if/else` chain?
