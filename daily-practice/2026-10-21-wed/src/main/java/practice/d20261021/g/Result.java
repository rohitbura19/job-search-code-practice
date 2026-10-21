package practice.d20261021.g;

import java.util.List;

/** GIVEN - hits (closest first) plus cost counters so tests can see how much work a search did. */
public record Result(List<Hit> hits, int cellsVisited, int examined) {}
