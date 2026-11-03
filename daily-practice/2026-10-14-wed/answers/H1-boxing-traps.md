# Answers: H1-boxing-traps

(Spoilers. Open only after your attempt.)

**Reference** (all outputs verified by running the code)
- Line 1: `true false true`. `Integer.valueOf` caches -128..127 (the range guaranteed by the spec, the upper bound can be raised with `-XX:AutoBoxCacheMax`), so 127 is one shared object and 128 is two. `==` compares references; always use `equals` for boxed values. The cache exists to save allocations for small, very common values.
- Line 2: `false true true`. `Long.equals(Integer)` is false because the argument is boxed to `Integer` and `equals` checks the class. `L == 127` unboxes to a primitive comparison, so true.
- Line 3: `NullPointerException`. With an `int` operand in the ternary, the whole expression has type `int`, so `nul` is unboxed. Line 4 is fine and gives `null`: both operands are `Integer`, no unboxing.
- Line 5: `a 97`. If one operand is `char` and the other an int CONSTANT that fits in `char`, the result type is `char`; with a non-constant `int` variable it promotes to `int` and prints 97.
- Line 6: `1.0`. Binary numeric promotion applies in a ternary with two numeric operands, so the `1` became a `double`. (`true ? 1 : "x"` prints `1` since there is no numeric promotion with a String.)
- Line 7: `true`. After erasure there is one `ArrayList` class.
- Line 8: `sh += 1` compiles (compound assignment has an implicit narrowing cast, value 2); `sh = sh + 1` does not (int to short, lossy).
- Line 9: `false -2147483648 -2147483648`. Floating point is binary, use `BigDecimal` for money; `abs(MIN_VALUE)` overflows; `MAX_VALUE + 1` wraps.
- Line 10: `-3 -1 1 -4 15`. Integer division truncates toward zero; `%` keeps the sign of the dividend; `floorMod` is the mathematical modulus (use it for ring-buffer or hash indexes); `>>` is arithmetic (sign extends), `>>>` is logical.
- Erasure: generic type arguments are removed at compile time and casts inserted. So you cannot: `new T()`, `new T[10]`, `instanceof List<String>`, overload `m(List<String>)` and `m(List<Integer>)`, use primitives as type arguments, or catch a generic exception type. PECS: "Producer Extends, Consumer Super": read from `List<? extends Number>`, write into `List<? super Integer>` (this is the signature shape of `Collections.copy`).
- `HashMap<Integer,String>.get(5L)` compiles (the parameter is `Object`) and returns `null` because a `Long` never equals an `Integer`. A classic production bug when an id comes out of JSON as a `Long`/`Integer` mix.
