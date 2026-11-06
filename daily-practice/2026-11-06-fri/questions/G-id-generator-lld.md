# G - LLD of F1: Snowflake id generator (30 min: sketch 8, code 22)
File: `g/SnowflakeIdGenerator.java`. Layout: 41 bits ms since a custom epoch | 10 bits worker | 12 bits sequence. The clock is injected as a `LongSupplier` so tests can control it.
Rules and error cases are in the Javadoc of each method. Think about: why `synchronized` is enough here, what happens if the clock goes backwards 2 ms (you throw; what would a production fix be?), and how a sequence overflow is handled without sleeping a whole millisecond blindly.
Sketch first in `my-designs/G-classes-sketch.md`.
