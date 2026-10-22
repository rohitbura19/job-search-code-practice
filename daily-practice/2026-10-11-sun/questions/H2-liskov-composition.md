> Code: `h2/` — read the GIVEN `BrokenRectangle` / `BrokenSquare`, then write `Rect.java`, `Sq.java` (the redesign; the `Shape` interface is GIVEN), `Amphibian.java` (default-method diamond) and `RetryingHttpClient.java` (composition). Test: `LiskovCompositionTest`. Items 3 and 4 of the question (abstract class vs interface, why composition) are also on paper.

### H2. OOPS: Inheritance vs composition, and Liskov
```java
class Rectangle { protected int w, h; void setW(int w){this.w=w;} void setH(int h){this.h=h;} int area(){return w*h;} }
class Square extends Rectangle { void setW(int w){this.w=this.h=w;} void setH(int h){this.w=this.h=h;} }
// client
void grow(Rectangle r){ r.setW(5); r.setH(4); assert r.area()==20; }
```
1. What principle does `Square` break, and what does `grow(new Square())` return?
2. Redesign it.
3. Abstract class vs interface with default methods in Java 8+: when each? How does Java resolve the diamond problem with default methods?
4. Why is "prefer composition over inheritance" a rule of thumb? Give one backend example.

Kit contract for the code part:
- `Shape` (GIVEN): `int area()` and `int perimeter()`. `Rect(int w, int h)` and `Sq(int side)` are records implementing it, immutable: `Rect.withW(int)` / `withH(int)` return a NEW `Rect`; `Sq.withSide(int)` returns a new `Sq`.
- `Amphibian implements Walker, Swimmer` (both GIVEN, both have a default `move()` returning `"walk"` / `"swim"`): `move()` must return `"walk+swim"` by calling both `super` defaults.
- `RetryingHttpClient(HttpClient delegate, int maxAttempts)` implements the GIVEN `HttpClient` (`String get(String url)`): a decorator that calls the delegate up to `maxAttempts` times while it throws a `RuntimeException`, and rethrows the last exception when all attempts fail. `maxAttempts < 1` throws `IllegalArgumentException` from the constructor.
