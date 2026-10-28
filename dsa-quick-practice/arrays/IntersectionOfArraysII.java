package arrays;

import java.util.*;

// Intersection of Two Arrays II  [Easy | Hashing or sort + two pointers]
// Return the common elements, each as many times as it appears in both, ascending.
//
// [1,2,2,1] & [2,2]       -> [2,2]
// [4,9,5] & [9,4,9,8,4]   -> [4,9]

public class IntersectionOfArraysII {

    static int[] intersect(int[] a, int[] b) {


        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> result = new ArrayList<>();

        for(int i:a){
            if(map.containsKey(i)){
                map.put(i, map.get(i)  + 1);
            }else{
                map.put(i, 1);
            }
        }

        for(int i:b){
            if(map.containsKey(i)){
                int temp = map.get(i);
                if(temp> 0){
                    map.put(i, temp -1);
                    result.add(i);
                }else{
                    map.remove(i);
                }
            }
        }


        if(result.size()> 0){
            return result.stream().mapToInt(Integer::intValue).sorted().toArray();
        }

        
        

        // Map<Integer, Integer> map = new HashMap<>();

        // List<Integer> result = new ArrayList<>();

        // for(int i:a){
        //     if(map.containsKey(i)){
        //         map.put(i, map.get(i) + 1);
        //     }else{
        //         map.put(i, 1);
        //     }
        // }

        

        // for(int i:b){
        //     if(map.containsKey(i)){
        //         int temp = map.get(i);
        //         if(temp>= 1){
        //             map.put(i, temp - 1);
        //             result.add(i);
        //         }else{
        //             map.remove(i);
        //         }
        //     }
        // }


        // if(result.size() > 0){
        //     return result.stream().mapToInt(Integer::intValue).sorted().toArray();
        // }

        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(intersect(new int[]{1, 2, 2, 1}, new int[]{2, 2})));       // [2, 2]
        System.out.println(Arrays.toString(intersect(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4}))); // [4, 9]
    }
}
