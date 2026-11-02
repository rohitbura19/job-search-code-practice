package practice.d20261102.h1;

/** Reference answers: the exact output of H1Snippets.sN(). Explanations as comments. */
public final class H1Predictions {
    private H1Predictions() {}

    /** a.name uses the static type (fields are not virtual); static kind() binds statically, speak() is virtual. Inside Animal.who(), name and kind() are Animal's. Overloads: phase 1 (no boxing) picks f(long) for int/long/char, f(Object) for the Object-typed o even though it holds a String, boxing is phase 2 (1.5 -> Double -> Object), varargs is the last resort. */
    public static String s1() { return "animal,dog,animal,animal/Animal/woof,animal/Animal/woof,Object,String,long,long,long,Object,varargs,long"; }

    /** Resources close in reverse order; the body exception wins and close failures are attached as suppressed; finally runs after the catch. */
    public static String s2() { return "[open A, open B, body, close B, close A, finally] body-fail[close-B, close-A]"; }

    /** Streams are lazy and pull one element at a time through the whole pipeline (limit short-circuits after p3); sorted is a barrier that consumes everything first; the source is read only when the terminal operation runs, so the later add(3) is visible. */
    public static String s3() { return "[50, 30] [p5, p4, p3] [a3, a1, a2, b1] [2, 4, 6]"; }

    /** NaN != NaN but Double.equals(NaN) is true; -0.0 == 0.0 but Double.equals distinguishes them; Math.round is floor(x + 0.5); (long)1e19 saturates; (byte)200 wraps; float rounding makes 0.1f+0.2f==0.3f true. */
    public static String s4() { return "false,true,true,false,-2,3,-3,c,99,65,2.5,2,2,-1,true,true,Infinity,9223372036854775807,-56"; }

    /** subList is a view (set writes through); a structural change through the parent invalidates the sublist (CME on next use); Iterator.remove is safe; removing the second-to-last element in a for-each ends the loop silently because hasNext() is false; PriorityQueue.poll is sorted; a TreeMap comparator defines key equality (("bb" equals "aa" by length, the key stays "aa", the value is replaced). */
    public static String s5() { return "[20, 3, 40][1, 20, 3, 40, 5] CME [1, 3] no-exception [1, 3] 12345 {c=3, aa=2}"; }

    /** record toString/equals are component-wise but an array component compares by reference; pattern switch picks the first matching case in order; guards; case null; old-style switch falls through, default may sit in the middle. */
    public static String s6() { return "Rect[w=2, h=3],true,true,false,big circle,circle 3,square-ish 4,rect 10,square 7,level 2,null!,other,111,110,100,10000,11000,-1,3"; }

}
