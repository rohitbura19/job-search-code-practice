package graphs;

// Course Schedule  [Medium | Cycle detection with Kahn's algorithm]
// prereq[i] = {course, mustTakeFirst}. Can all n courses be finished?
//
// n=2, [[1,0]]        -> true
// n=2, [[1,0],[0,1]]  -> false

public class CourseSchedule {

    static boolean canFinish(int n, int[][] prereq) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(canFinish(2, new int[][]{{1, 0}}));         // true
        System.out.println(canFinish(2, new int[][]{{1, 0}, {0, 1}})); // false
    }
}
