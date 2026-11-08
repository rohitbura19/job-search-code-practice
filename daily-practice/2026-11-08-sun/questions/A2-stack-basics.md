# A2 - Stacks (10 min) - `a2/StackBasics.java`

1. `evalRpn(String[] tokens)`: `+ - * /` and integers; `/` truncates toward zero; input is valid. `["2","1","+","3","*"]` -> 9; `["4","13","5","/","+"]` -> 6.
2. `MinStack`: push, pop, top, getMin all O(1); `Integer.MIN_VALUE` allowed. Empty pop/top/getMin -> `NoSuchElementException`.
3. `QueueViaStacks`: FIFO queue from two stacks, amortised O(1); empty pop/peek -> `NoSuchElementException`.
