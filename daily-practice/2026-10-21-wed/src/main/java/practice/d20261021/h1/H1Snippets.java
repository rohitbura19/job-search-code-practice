package practice.d20261021.h1;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * GIVEN CODE - do not edit. Each method runs one puzzle from questions/H1 and returns what it "printed".
 * Read the code, predict the output, and write your prediction into {@link H1Predictions}.
 */
public final class H1Snippets {
    private H1Snippets() {}

    public static String line1() {
        StringBuilder out = new StringBuilder();
        List<Runnable> rs = new ArrayList<>();
        for (int i = 0; i < 3; i++) { int j = i; rs.add(() -> out.append(j).append(' ')); }
        for (String s : List.of("a", "b")) rs.add(() -> out.append(s).append(' '));
        rs.forEach(Runnable::run);
        return out.toString();
    }

    public static String line2() {
        int[] c = {0};
        Runnable inc = () -> c[0]++;
        inc.run(); inc.run();
        return String.valueOf(c[0]);
    }

    static class Greeter {
        String name = "outer";
        StringBuilder out;
        Greeter(StringBuilder out) { this.out = out; }
        Runnable lam() { return () -> out.append(this.name).append(' ').append(getClass().getSimpleName()); }
        Runnable anon() {
            return new Runnable() {
                String name = "anon";
                public void run() { out.append(this.name).append(' ').append(getClass().getSimpleName().isEmpty()); }
            };
        }
    }

    public static String line3() {
        StringBuilder out = new StringBuilder();
        Greeter g = new Greeter(out);
        g.lam().run(); out.append(" | "); g.anon().run();
        return out.toString();
    }

    public static String line4() {
        StringBuilder out = new StringBuilder();
        String s = null;
        Supplier<String> late = () -> s.toString();
        out.append("lambda created; ");
        try { late.get(); } catch (NullPointerException e) { out.append("NPE at call; "); }
        try { Supplier<String> early = s::toString; out.append("ref created"); }
        catch (NullPointerException e) { out.append("NPE at creation of the method reference"); }
        return out.toString();
    }

    public static String line5() {
        StringBuilder[] holder = {new StringBuilder("first")};
        Supplier<String> viaRef = holder[0]::toString;
        Supplier<String> viaLam = () -> holder[0].toString();
        holder[0] = new StringBuilder("second");
        return viaRef.get() + " " + viaLam.get();
    }

    public static String line6() {
        StringBuilder out = new StringBuilder();
        Supplier<String> expensive = () -> { out.append("expensive "); return "x"; };
        Optional.of("v").orElse(expensive.get());
        out.append("| ");
        Optional.of("v").orElseGet(expensive);
        out.append("| ");
        out.append(Optional.of(1).map(i -> (String) null).isPresent()).append(' ');
        try { Optional.of(null); } catch (NullPointerException e) { out.append("NPE "); }
        try { Optional.empty().get(); } catch (NoSuchElementException e) { out.append("NSEE"); }
        return out.toString();
    }

    public static String line7() {
        Function<Integer, Integer> f = x -> x + 1, g = x -> x * 2;
        return f.andThen(g).apply(5) + " " + f.compose(g).apply(5);
    }

    static void run(Runnable r, StringBuilder out) { out.append("Runnable "); }
    static <T> void run(Callable<T> c, StringBuilder out) { out.append("Callable "); }
    static int five() { return 5; }

    public static String line8() {
        StringBuilder out = new StringBuilder();
        run(() -> { throw new RuntimeException(); }, out);
        run(() -> System.out.print(""), out);
        run(() -> "x", out);
        run(() -> { }, out);
        run(() -> five(), out);
        return out.toString();
    }

    public static String line10() throws InterruptedException {
        StringBuilder out = new StringBuilder();
        ExecutorService ex = Executors.newSingleThreadExecutor();
        try {
            Future<?> fu = ex.submit(() -> { throw new IllegalStateException("boom"); });
            out.append("submitted; ");
            try { fu.get(); } catch (ExecutionException e) { out.append(e.getCause().getClass().getSimpleName()).append(' ').append(e.getCause().getMessage()); }
        } finally { ex.shutdown(); }
        return out.toString();
    }
}
