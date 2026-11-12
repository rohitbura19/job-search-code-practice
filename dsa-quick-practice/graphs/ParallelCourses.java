package graphs;

// Parallel Courses (minimum semesters)  [Medium | Kahn's algorithm, count BFS levels]
// prereq[i] = {course, mustTakeFirst}, courses 0..n-1. Any number per semester. -1 on a cycle.
//
// n=3, [[1,0],[2,1]]        -> 3
// n=3, [[2,0],[2,1]]        -> 2
// n=2, [[1,0],[0,1]]        -> -1

public class ParallelCourses {

    static int minSemesters(int n, int[][] prereq) {
        // TODO
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(minSemesters(3, new int[][]{{1, 0}, {2, 1}})); // 3
        System.out.println(minSemesters(3, new int[][]{{2, 0}, {2, 1}})); // 2
        System.out.println(minSemesters(2, new int[][]{{1, 0}, {0, 1}})); // -1
    }
}
