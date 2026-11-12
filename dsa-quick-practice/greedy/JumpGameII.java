package greedy;

// Jump Game II  [Medium | BFS-style ranges]
// nums[i] = max jump from i. Fewest jumps to reach the last index. O(n), O(1).
//
// [2,3,1,1,4] -> 2
// [2,3,0,1,4] -> 2

public class JumpGameII {

    static int jump(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(jump(new int[]{2, 3, 1, 1, 4})); // 2
        System.out.println(jump(new int[]{2, 3, 0, 1, 4})); // 2
    }
}
