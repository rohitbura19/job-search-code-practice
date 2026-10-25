> Code: `src/main/java/practice/d20261025/c2/Accounts.java`. Test: `AccountsTest`.

# C2. Accounts Merge with Union-Find (25 min)

(a) `merge(List<List<String>> accounts)`: each account is `[name, email...]`. Accounts sharing an email (transitively) are one person (same name). Return `[name, sorted distinct emails...]` per person, ordered by the smallest email. Names can repeat across different people. Do not modify the input.

(b) `redundantLinks(accounts)`: the number of email-sharing links that join two accounts already connected (each closes a cycle).

Example: `[John, a, b] [John, a, c] [Mary, m] [John, j]` -> `[John, a, b, c] [John, j] [Mary, m]`.

Constraints: up to 2*10^5 accounts in one chain must finish in 4 s (no recursion depth problems, no quadratic merging).

Say out loud: path compression + union by size and what they give together; why you union ACCOUNTS (not emails) here and how the other formulation works; why recursive DFS would overflow the stack on a long chain.
