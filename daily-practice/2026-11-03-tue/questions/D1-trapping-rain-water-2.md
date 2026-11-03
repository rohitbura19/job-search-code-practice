# D1. Trapping Rain Water II (20 min)

> Code: `src/main/java/practice/d20261103/d1/TrappingRain2.java`. Test: `TrappingRain2Test`.

Given an `m x n` grid of heights (1 <= m, n <= 300, 0 <= h <= 20_000), water flows in 4 directions and spills off the border. Return the volume trapped after rain.

Example: `{{1,4,3,1,3,2},{3,2,1,3,2,4},{2,3,3,2,3,1}}` -> 4; a 5x5 ring of 3s around a basin (2s, centre 1) -> 10.

Hint to find yourself: what decides the water level of a cell, and from where should you start the flood? Target O(mn log(mn)). Explain why the 1D two-pointer trick does not generalise.
