> Code: `a2/ValidParentheses.java` (`isValid`, then the follow-up `firstMismatch`). Test: `ValidParenthesesTest`.

### A2. Valid Parentheses (Stack)
String of `()[]{}`. Return true if brackets close in the right order and type.
Example: `"{[()]}" -> true`, `"(]" -> false`, `"((" -> false`.

Interviewer follow-up: return the index of the first mismatch. Extend to ignore non-bracket characters (log lines).

(Kit contract for the follow-up: `firstMismatch(s)` ignores every non-bracket character and returns `-1` if balanced, the index of the first closer that does not match (or has nothing to close), or `s.length()` if openers are left unclosed.)
