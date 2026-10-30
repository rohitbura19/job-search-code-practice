# C2. Greedy with counts and a heap: task cooldown, no adjacent equal (20 min)

Implement in `c2/Scheduling.java`.

1. `leastInterval(char[] tasks, int n)`: minimum time to run all tasks with a cooldown of `n` between equal tasks; idle slots count. `AAABBB`, n=2 -> 8.
2. `reorganize(String s)`: rearrange so no two adjacent characters are equal; `""` if impossible. `aab` -> `aba`.

Constraints: 10^6 tasks / 10^5 characters. The test compares `leastInterval` with a BFS over schedules for tiny inputs.
Say out loud: what is the closed-form for (1), and what exact condition makes (2) impossible?
