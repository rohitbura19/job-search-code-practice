package backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Combination Sum  [Medium | Backtracking]
// Distinct positive candidates, each reusable. All unique combinations summing to target.
//
// [2,3,6,7], 7 -> [[2,2,3],[7]]
// [2,3,5], 8   -> [[2,2,2,2],[2,3,3],[3,5]]

public class CombinationSum {

    static List<List<Integer>> combinationSum(int[] candidates, int target) {
        // metrix[i][j] = can we make sum j using the first i candidates (each reusable)?
        Arrays.sort(candidates);
        boolean[][] metrix = new boolean[candidates.length+1][target+1];

        for(int i=0;i<=target;i++){
            metrix[0][i] = false;
        }

        for(int i=0;i<=candidates.length;i++){
            metrix[i][0] = true;
        }

        for(int i=1;i<=candidates.length;i++){
            int c = candidates[i-1];
            for(int j=0;j<=target;j++){
                if(j >= c){
                    // skip candidate i, OR use it once more (stay on row i because it is reusable)
                    metrix[i][j] = metrix[i-1][j] || metrix[i][j-c];
                }else{
                    // candidate too big for this sum: only option is to skip it
                    metrix[i][j] = metrix[i-1][j];
                }
            }
        }

        // the table only says yes/no, so walk it backwards to collect every combination
        List<List<Integer>> result = new ArrayList<>();
        collect(candidates, metrix, candidates.length, target, new ArrayList<>(), result);
        return result;
    }

    static void collect(int[] candidates, boolean[][] metrix, int i, int j,
                        List<Integer> path, List<List<Integer>> result) {
        if(j == 0){
            List<Integer> combo = new ArrayList<>(path);
            Collections.reverse(combo); // path was built largest-first
            result.add(combo);
            return;
        }
        if(i == 0) return;

        int c = candidates[i-1];
        // branch 1: sum j is reachable without candidate i
        if(metrix[i-1][j]){
            collect(candidates, metrix, i-1, j, path, result);
        }
        // branch 2: sum j is reachable by using candidate i once more
        if(j >= c && metrix[i][j-c]){
            path.add(c);
            collect(candidates, metrix, i, j-c, path, result);
            path.remove(path.size()-1);
        }
    }

    public static void main(String[] args) {
        System.out.println(combinationSum(new int[]{2, 3, 6, 7}, 7)); // [[2, 2, 3], [7]]
        System.out.println(combinationSum(new int[]{2, 3, 5}, 8));    // [[2, 2, 2, 2], [2, 3, 3], [3, 5]]
    }
}
