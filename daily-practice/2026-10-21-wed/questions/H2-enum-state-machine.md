> Code: `h2/ChargerState.java` (each constant body), `h2/Code.java`. Test: `ChargerStateTest`. Predict lines 2 to 5 on paper.

### H2. OOPS: enums as polymorphic state machines (constant-specific bodies), `valueOf`, `ordinal` and `values()` traps
```java
enum Event { PLUG_IN, AUTHORIZED, STOP, PLUG_OUT, FAULT, RESET }
enum ChargerState {
    AVAILABLE { ChargerState on(Event e) { return e == Event.PLUG_IN ? PREPARING : this; } },
    PREPARING { ChargerState on(Event e) { return switch (e) { case AUTHORIZED -> CHARGING; case PLUG_OUT -> AVAILABLE; case FAULT -> FAULTED; default -> this; }; } },
    CHARGING  { ChargerState on(Event e) { return switch (e) { case STOP -> FINISHING; case FAULT -> FAULTED; case PLUG_OUT -> AVAILABLE; default -> this; }; }
                @Override boolean drawsPower() { return true; } },
    FINISHING { ChargerState on(Event e) { return switch (e) { case PLUG_OUT -> AVAILABLE; case FAULT -> FAULTED; default -> this; }; } },
    FAULTED   { ChargerState on(Event e) { return e == Event.RESET ? AVAILABLE : this; } };
    abstract ChargerState on(Event e);
    boolean drawsPower() { return false; }
}
ChargerState s = ChargerState.AVAILABLE;
for (Event e : new Event[]{PLUG_IN, AUTHORIZED, STOP, FAULT, PLUG_OUT, RESET, PLUG_OUT}) { s = s.on(e); print(s + " "); }          // line 1
print((CHARGING.getClass() == ChargerState.class) + " " + (CHARGING.getDeclaringClass() == ChargerState.class) + " [" + CHARGING.getClass().getSimpleName() + "] "
      + ChargerState.class.isEnum() + " " + CHARGING.getClass().isEnum() + " " + CHARGING.drawsPower() + FAULTED.drawsPower());          // line 2
// line 3: ChargerState.valueOf("charging")  ?   ChargerState.valueOf(null)  ?   switch (nullState) { ... }  ?   nullState == AVAILABLE  ?
print((ChargerState.values() == ChargerState.values()) + " " + CHARGING.compareTo(AVAILABLE) + " " + CHARGING.ordinal());               // line 4
enum Code { ONLINE("O"), OFFLINE("F"); static int count = 0;  Code(String c) { count++; } }                                           // line 5: does this compile?
```
1. Predict every line, with the reason. Why does every constant have to implement `on` (what stops you from forgetting one)?
2. What is a constant-specific class body? What is the runtime class of `CHARGING`, and what does `getDeclaringClass()` return instead? Why can an enum not be extended, and what CAN it do (fields, constructors, interfaces, abstract methods)?
3. Compare three designs for the same behaviour: (a) this enum with an abstract `on`, (b) one `switch` on the state in a service, (c) a transition table `EnumMap<State, EnumMap<Event, State>>`, and (d) sealed interface + records when states carry DIFFERENT data. Open/closed, exhaustiveness checking, where the knowledge lives, testability.
4. Why are `ordinal()` and `EnumType.ORDINAL` dangerous to persist, and what do you store instead? Why does `values()` allocate every time? Why is `==` the right comparison for enums? How is an enum serialised, and why does it survive Java serialisation as a singleton (the 20 Oct singleton sheet)?
5. State pattern vs this enum: when does a state need its own fields and collaborators (then a class per state or a sealed record), and when is an enum with behaviour enough?
