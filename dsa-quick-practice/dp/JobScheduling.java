package dp;

// Maximum Profit in Job Scheduling  [Hard | Sort by end + DP + binary search]
// Pick non-overlapping jobs for max profit. A job may start when another ends.
//
// start=[1,2,3,3], end=[3,4,5,6], profit=[50,10,40,70] -> 120
// start=[1,1,1],   end=[2,3,4],   profit=[5,6,4]       -> 6

public class JobScheduling {

    static int jobScheduling(int[] start, int[] end, int[] profit) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(jobScheduling(new int[]{1, 2, 3, 3}, new int[]{3, 4, 5, 6}, new int[]{50, 10, 40, 70})); // 120
        System.out.println(jobScheduling(new int[]{1, 1, 1}, new int[]{2, 3, 4}, new int[]{5, 6, 4}));             // 6
    }
}
