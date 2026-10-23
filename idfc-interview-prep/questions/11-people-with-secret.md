# Q11. Find All People With the Secret

**Duration:** 30 minutes · **Language:** Java · **Source:** IDFC Senior Developer coding round, per LeetCode (LeetCode 2092)

## Problem Statement

There are `n` people, numbered `0..n-1`. Person `0` knows a secret, and at time `0` tells it to `firstPerson`.

You are given `meetings[i] = [x, y, time]`: persons `x` and `y` meet at `time`. Whenever two people meet and one of them knows the secret, the other learns it too. Sharing happens **instantly**: a person can learn the secret and pass it on in **other meetings at the same time**. Someone who only learns it at a later time can't share it in an earlier meeting.

Return every person who knows the secret after all meetings, sorted in ascending order.

```java
List<Integer> findAllPeople(int n, int[][] meetings, int firstPerson)
```

## Examples

```
n=6, meetings=[[1,2,5],[2,3,8],[1,5,10]], firstPerson=1        -> [0,1,2,3,5]
  t=0: 0->1;  t=5: 1->2;  t=8: 2->3;  t=10: 1->5

n=4, meetings=[[3,1,3],[1,2,2],[0,3,3]], firstPerson=3         -> [0,1,3]
  t=2: 1 and 2 meet, but neither knows yet;  t=3: 3->1 (and 0-3 again)

n=5, meetings=[[3,4,2],[1,2,1],[2,3,1]], firstPerson=1         -> [0,1,2,3,4]
  t=1: 1->2, then 2->3 at the same time;  t=2: 3->4

n=4, meetings=[[1,2,1],[0,1,2]], firstPerson=3                  -> [0,1,3]
  the meeting at t=1 happens before 1 knows, so 2 never learns it
```

## Constraints

`2 ≤ n ≤ 10^5`, `1 ≤ meetings.length ≤ 10^5`, `1 ≤ time ≤ 10^5`, `x ≠ y`.

## Expected Approach (explain the trade-off)

- Sort the meetings by time and **process each group with the same time together**.
- **Option 1, BFS or DFS per time group:** build a small graph for the group and start from the people in it who already know.
- **Option 2, Union-Find with reset:** union everyone in the group with person 0's set, then **reset** the people in the group who didn't end up connected to 0. This reset is the key insight, and it's the reason the problem is hard.
- O(M log M) for the sort plus about O(M α(n)).

## Edge cases

Many meetings at the same time forming a chain · `firstPerson` with no meetings · the same pair meeting at several times · an unordered `meetings` input.

## Follow-ups

1. Return the time each person learned the secret.
2. Meetings arrive as a live stream in time order. What changes?
3. Relate it to a real system: spreading a fraud flag or a compromised credential through a network of accounts that transacted together.
