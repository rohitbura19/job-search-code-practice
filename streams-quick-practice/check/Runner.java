package check;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/** Runs every @Test method of a check class and prints OK / TODO / WRONG per method. */
public final class Runner {
    private Runner() {}

    public static void run(Class<?> checks) {
        Method[] methods = checks.getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));
        int ok = 0, todo = 0, wrong = 0;
        for (Method m : methods) {
            if (!m.isAnnotationPresent(Test.class)) continue;
            DisplayName dn = m.getAnnotation(DisplayName.class);
            String name = dn != null ? m.getName() + " (" + dn.value() + ")" : m.getName();
            try {
                m.setAccessible(true);
                Object target = null;
                if (!java.lang.reflect.Modifier.isStatic(m.getModifiers())) {
                    Constructor<?> c = checks.getDeclaredConstructor();
                    c.setAccessible(true);
                    target = c.newInstance();
                }
                m.invoke(target);
                System.out.println("OK    " + name);
                ok++;
            } catch (InvocationTargetException e) {
                Throwable t = e.getCause();
                if (isTodo(t)) {
                    System.out.println("TODO  " + name);
                    todo++;
                } else {
                    System.out.println("WRONG " + name + " -> " + t);
                    wrong++;
                }
            } catch (ReflectiveOperationException e) {
                System.out.println("ERROR " + name + " -> " + e);
                wrong++;
            }
        }
        System.out.printf("%n%d ok, %d wrong, %d todo%n", ok, wrong, todo);
    }

    private static boolean isTodo(Throwable t) {
        for (Throwable c = t; c != null; c = c.getCause()) {
            if (c instanceof UnsupportedOperationException && "TODO".equals(c.getMessage())) return true;
        }
        return false;
    }
}
