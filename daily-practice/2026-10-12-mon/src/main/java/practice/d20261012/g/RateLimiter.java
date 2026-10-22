package practice.d20261012.g;

/** GIVEN - the Strategy interface every algorithm implements. */
public interface RateLimiter {
    Decision tryAcquire(String key);
}
