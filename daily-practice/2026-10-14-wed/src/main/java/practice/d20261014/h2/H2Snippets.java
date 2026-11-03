package practice.d20261014.h2;

import java.util.ArrayList;
import java.util.List;

/**
 * GIVEN CODE - do not edit. The puzzle from questions/H2, with System.out replaced by a log.
 * firstInstance() is the FIRST "new Child()" in this JVM, secondInstance() is the next one.
 * Both return the log lines joined with "|". Predict them in {@link H2Predictions}.
 */
public final class H2Snippets {
    private H2Snippets() {}

    static final List<String> LOG = new ArrayList<>();

    static class Parent {
        static { LOG.add("P-static"); }
        { LOG.add("P-inst"); }
        Parent() { LOG.add("P-ctor"); show(); }
        void show() { LOG.add("P-show"); }
    }

    static class Child extends Parent {
        static { LOG.add("C-static"); }
        int v = 7;
        { LOG.add("C-inst"); }
        Child() { super(); LOG.add("C-ctor v=" + v); }
        @Override void show() { LOG.add("C-show v=" + v); }
    }

    private static final String[] RESULTS = run();   // runs once, in this order, so the tests cannot disturb each other

    private static String[] run() {
        LOG.clear();
        new Child();
        String first = String.join("|", LOG);
        LOG.clear();
        new Child();
        String second = String.join("|", LOG);
        return new String[]{first, second};
    }

    public static String firstInstance() { return RESULTS[0]; }
    public static String secondInstance() { return RESULTS[1]; }
}
