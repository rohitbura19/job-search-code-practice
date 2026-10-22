> Code: `c1/Islands.java` and `c1/IslandCounter.java` (the Number of Islands II follow-up). Test: `IslandsTest` (a 1000 x 1000 all-land grid overflows a recursive DFS; the input must not be modified). **15 min timer, plain editor.**

### C1. Number of Islands (Grid DFS/BFS)
`char[][] grid` of `'1'` (land) and `'0'` (water). Count islands (4-directional connectivity).
```
11000
11000
00100
00011   -> 3
```
Interviewer follow-up: grid is 10^4 x 10^4, recursion overflows — what now? Land cells are added one at a time and you must report the island count after each (Number of Islands II). Grid does not fit on one machine?
