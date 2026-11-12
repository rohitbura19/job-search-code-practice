package heap;

// Task Scheduler  [Medium | Count max frequency (or heap simulation)]
// Same task needs n units between runs. Minimum total time including idle.
//
// [A,A,A,B,B,B], n=2 -> 8
// [A,A,A,B,B,B], n=0 -> 6
// [A,A,A,A,B],   n=1 -> 7

public class TaskScheduler {

    static int leastInterval(char[] tasks, int n) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(leastInterval("AAABBB".toCharArray(), 2)); // 8
        System.out.println(leastInterval("AAABBB".toCharArray(), 0)); // 6
        System.out.println(leastInterval("AAAAB".toCharArray(), 1));  // 7
    }
}
