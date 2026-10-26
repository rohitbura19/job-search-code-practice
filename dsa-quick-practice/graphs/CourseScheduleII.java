package graphs;

import java.util.Arrays;

// Course Schedule II  [Medium | Topological sort (Kahn BFS)]
// prerequisites[i] = [a, b]: take b before a. Return any valid order, or [] if there is a cycle.
//
// n=4, [[1,0],[2,0],[3,1],[3,2]] -> [0,1,2,3] or [0,2,1,3]
// n=2, [[1,0],[0,1]]             -> []

public class CourseScheduleII {

    static int[] findOrder(int numCourses, int[][] prerequisites) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(findOrder(4, new int[][]{{1, 0}, {2, 0}, {3, 1}, {3, 2}}))); // [0, 1, 2, 3]
        System.out.println(Arrays.toString(findOrder(2, new int[][]{{1, 0}, {0, 1}})));                 // []
    }
}
