# C2 - Greedy (medium, 20 min)
File: `c2/GreedyProblems.java`

1. **minJumps(nums)** - fewest jumps from index 0 to the end; `[2,3,1,1,4]` -> 2; unreachable -> -1 (e.g. `[1,0,1]`). n up to 10^6, O(n) O(1).
2. **canCompleteCircuit(gas, cost)** - circular route, return the unique start index or -1. `[1,2,3,4,5],[3,4,5,1,2]` -> 3. Sums can exceed int.
3. **partitionLabels(s)** - cut a lowercase string into the most parts such that no letter is in two parts. `"ababcbacadefegdehijhklij"` -> `[9,7,8]`.
For each: state the greedy choice and why it is safe (exchange argument) in one sentence.
