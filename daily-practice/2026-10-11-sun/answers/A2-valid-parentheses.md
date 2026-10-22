# Answers: A2-valid-parentheses

(Spoilers. Open only after your attempt.)

**Reference**
```java
public boolean isValid(String s) {
    Deque<Character> st = new ArrayDeque<>();   // not Stack (synchronized, legacy)
    for (char c : s.toCharArray()) {
        switch (c) {
            case '(' -> st.push(')');
            case '[' -> st.push(']');
            case '{' -> st.push('}');
            default -> { if (st.isEmpty() || st.pop() != c) return false; }
        }
    }
    return st.isEmpty();
}
```
O(n) time, O(n) space. Trap: forgetting the final `isEmpty()` check.
