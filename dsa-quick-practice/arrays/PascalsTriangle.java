package arrays;

import java.util.ArrayList;
import java.util.List;

// Pascal's Triangle  [Easy | Row by row]
// Return the first numRows rows.
//
// 5 -> [[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]

public class PascalsTriangle {

    static List<List<Integer>> generate(int numRows) {
        

        List<List<Integer>> result = new ArrayList<>();

        List<Integer> prev = null;

        for(int i=0;i<numRows;i++){
            List<Integer> temp = new ArrayList<>();

            temp.add(1);


            if(i>1){
                prev = result.get(result.size()-1);
                for(int j=0;j<prev.size()-2;j++){
                    int first = prev.get(j);
                    int second = prev.get(j+1);
                    temp.add(first + second);
                }
            }



            if(i>0){
                temp.add(1);
            }

            result.add(temp);


        }

        if(result.size()> 0)return result;
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(generate(5)); // [[1], [1, 1], [1, 2, 1], [1, 3, 3, 1], [1, 4, 6, 4, 1]]
    }
}
