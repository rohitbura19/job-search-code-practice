package practice.d20261025.g;

/** Unique, increasing non-negative ids (in production: a block-allocating counter service). */
public interface IdGenerator {
    long next();
}
