# D1 - Count of Smaller Numbers After Self (20 min, hard) - `d1/CountSmaller.java`

`countSmallerAfter(int[] a)`: `res[i]` = number of `j > i` with `a[j] < a[i]`. `[5,2,6,1] -> [2,1,1,0]`. n up to 200,000, values any int (duplicates, `MIN_VALUE`, `MAX_VALUE`); the O(n^2) double loop times out. Use merge sort on an index array (count how many right-half elements jump ahead of each left-half element) or a Fenwick tree over compressed values.
