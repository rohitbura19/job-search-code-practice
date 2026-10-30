# G. Classes sketch: InMemoryBroker (10 min, before coding)

## Fields (and which lock guards each)

## How a record's partition and offset are chosen (keyed, null key)

## How the assignment is derived (and why storing it would be a bug source)

## Invariants (offset == index in the log; committed <= log end; committed never decreases)

## poll / commit step by step (what is checked in which order)

## What can go wrong under concurrency, and what the tests will try
