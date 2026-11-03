> Code: read `src/main/java/practice/d20261014/h1/H1Snippets.java`, type your predictions into `H1Predictions.java`. Test: `H1PredictionsTest`.

### H1. Core Java: boxing, caching, ternary typing, erasure
```java
Integer i1 = 127, i2 = 127, i3 = 128, i4 = 128;
System.out.println((i1 == i2) + " " + (i3 == i4) + " " + i3.equals(i4));          // line 1

Long L = 127L;
System.out.println(L.equals(127) + " " + L.equals(127L) + " " + (L == 127));        // line 2

boolean flag = true; Integer nul = null;
int r = flag ? nul : 0;                                                            // line 3: what happens?
Integer r2 = flag ? nul : Integer.valueOf(0);                                      // line 4: and here?

char ch = 'a'; int zero = 0;
System.out.println((true ? ch : 0) + " " + (true ? ch : zero));                    // line 5
Object o2 = true ? 1 : 2.0;  System.out.println(o2);                               // line 6

System.out.println(new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass());  // line 7
short sh = 1; sh += 1;   // compiles?   sh = sh + 1;  // compiles?                 // line 8
System.out.println((0.1 + 0.2 == 0.3) + " " + Math.abs(Integer.MIN_VALUE) + " " + (Integer.MAX_VALUE + 1));  // line 9
System.out.println((-7 / 2) + " " + (-7 % 2) + " " + Math.floorMod(-7, 2) + " " + (-7 >> 1) + " " + (-7 >>> 28)); // line 10
```
1. Predict every line, with the reason. (In `H1Snippets`, line 3 returns `"NPE"` if it throws and line 4 the printed value; line 8 only runs the `sh += 1` half, the `sh = sh + 1` question is on paper; line 11 is item 4 below.)
2. Why does the Integer cache exist, what range is guaranteed, and how do you change it?
3. What is type erasure, what can't you do because of it (name three things), and what is the PECS rule?
4. `HashMap<Integer,String>` with `get(long)` — what happens and why?
5. Why is `equals` on `Long` vs `Integer` false even for the same number, and what does that mean for a `Map<Long, ...>` that you query with an `int` literal?
