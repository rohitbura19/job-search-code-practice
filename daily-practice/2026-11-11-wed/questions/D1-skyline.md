# D1 - The Skyline Problem (20 min) - `d1/Skyline.java`

`buildings[i] = {left, right, height}` with `left < right`, `height >= 1`, sorted by `left`. Return the outline as key points `{x, height}`, left to right: a key point is where the maximum height changes (the left end of a horizontal segment). The last point has height 0, consecutive points never share a height and no x repeats.

`[[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]` -> `[[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]`.
`[[0,2,3],[2,5,3]]` -> `[[0,3],[5,0]]` (touching buildings of equal height merge).

Constraints: n up to 100,000; coordinates are non-negative ints (`right` may be `Integer.MAX_VALUE`); heights up to 10^9. Target O(n log n).
