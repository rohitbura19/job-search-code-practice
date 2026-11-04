package practice.d20261104.h2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Given. Marks an interface method whose result may be cached per (method, arguments) by Proxies.memoizing. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Memo {}
