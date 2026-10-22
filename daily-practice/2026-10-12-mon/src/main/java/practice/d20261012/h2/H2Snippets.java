package practice.d20261012.h2;

/**
 * GIVEN CODE - do not edit. Each method runs one puzzle from questions/H2 and returns what it "printed".
 * Predict the returned text and write it into {@link H2Predictions}.
 */
public final class H2Snippets {
    private H2Snippets() {}

    static class A {
        String who(Object o) { return "A-obj"; }
        String who(String s) { return "A-str"; }
    }
    static class B extends A {
        @Override String who(Object o) { return "B-obj"; }
        String who(Integer i) { return "B-int"; }
    }

    public static String line1() { A a = new B(); Object o = "hi"; return a.who(o); }
    public static String line2() { A a = new B(); return a.who("hi"); }
    public static String line3() { A a = new B(); return a.who(5); }

    static class P {
        String field = "P-field";
        static String name() { return "P"; }
        String id() { return "P-inst"; }
    }
    static class Q extends P {
        String field = "Q-field";
        static String name() { return "Q"; }
        @Override String id() { return "Q-inst"; }
    }

    /** P p = new Q(); static method, instance method, field read through the P reference, then through a Q reference. */
    @SuppressWarnings("static-access")
    public static String line4() {
        P p = new Q();
        return p.name() + " " + p.id() + " " + p.field + " " + ((Q) p).field;
    }
}
