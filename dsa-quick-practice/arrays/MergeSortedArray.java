package arrays;

import java.util.Arrays;

// Merge Sorted Array  [Easy | Two pointers from the end]
// a has length m + n; a[0..m) and b are sorted. Merge b into a in place.
//
// a=[1,2,3,0,0,0], m=3, b=[2,5,6], n=3 -> [1,2,2,3,5,6]

public class MergeSortedArray {

    static void merge(int[] a, int m, int[] b, int n) {
        // reverse order merge

        m--;// A
        n--;// B
        int ind = a.length-1;

        while(ind >m && n >=0){
            if(b[n] > a[m]){
                a[ind--] = b[n--];
            }else{
                a[ind--] = a[m--];
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 0, 0, 0};
        merge(a, 3, new int[]{2, 5, 6}, 3);
        System.out.println(Arrays.toString(a)); // [1, 2, 2, 3, 5, 6]
    }
}
