package practice.d20261012.g;

/** GIVEN - plan lookup for a key (hot-reloadable in real life). */
public interface LimitResolver {
    Limit resolve(String key);
}
