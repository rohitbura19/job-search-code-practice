package practice.d20261108.d1;

import java.util.*;

/**
 * Hierholzer's algorithm for an Eulerian path. Walk greedily along the smallest unused edge; when a node has no unused
 * edge left it is a dead end and can only be the tail of the route, so it is prepended to the answer on the way back.
 * The dead-end handling is what fixes the greedy "wrong branch" without backtracking. Iterative (explicit stack) so a
 * path of 300k tickets cannot overflow the call stack. O(E log E) because of the per-airport heaps, O(E) space.
 */
public final class ReconstructItinerary {
    private ReconstructItinerary() {}

    public static List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        for (List<String> t : tickets) graph.computeIfAbsent(t.get(0), k -> new PriorityQueue<>()).add(t.get(1));
        Deque<String> stack = new ArrayDeque<>();
        LinkedList<String> route = new LinkedList<>();
        stack.push("JFK");
        while (!stack.isEmpty()) {
            PriorityQueue<String> out = graph.get(stack.peek());
            if (out != null && !out.isEmpty()) stack.push(out.poll());
            else route.addFirst(stack.pop());
        }
        return route;
    }
}
