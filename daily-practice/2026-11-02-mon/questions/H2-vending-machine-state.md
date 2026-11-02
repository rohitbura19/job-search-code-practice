# H2. OOP: State pattern - a vending machine (20 min)

> Code: `src/main/java/practice/d20261102/h2/VendingMachine.java` (`Status` and `Dispensed` are given). Test: `VendingMachineTest`. **20 min.**

Implement the machine with the **State pattern**: a private `State` interface with one class per state (`IdleState`, `CreditState`, `OutOfServiceState`); the machine delegates `insert` and `select` to the current state, and states switch the machine's state. No `switch (status)` in the public methods. The rules are in the class Javadoc. Draw the transition table first (3 states x insert / select / refund / setOutOfService).

Then on paper: how does adding a `MAINTENANCE` state change the code compared with a boolean/enum flag? What makes the machine thread-unsafe and what is the minimal fix?
