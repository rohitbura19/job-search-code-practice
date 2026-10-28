> Code: `src/main/java/practice/d20261028/c1/WordDictionary.java`, `Autocomplete.java`. Test: `TrieTest`. **25 min.**

# C1. Trie: wildcard dictionary and top-3 autocomplete

1. `WordDictionary`: `addWord(word)`, `search(pattern)` where `.` matches any one letter and the whole word must match, `startsWith(prefix)`.
   After adding `bad, dad, mad`: `search(".ad")` true, `search("b..")` true, `search("pad")` false, `search("ba")` false, `startsWith("ba")` true.
2. `Autocomplete`: `record(sentence, times)` adds hits; `suggest(prefix)` returns up to 3 recorded sentences with that prefix, most hits first, ties alphabetical.
   `i love you`(5), `island`(3), `i love leetcode`(2), `ironman`(2), `iron man`(2): `suggest("i")` -> `[i love you, island, i love leetcode]`; `suggest("iron")` -> `[iron man, ironman]` (space sorts before letters).

Constraints: up to 10^5 words / 5*10^4 sentences, length <= 100. Prefixes may include spaces; an unknown prefix returns an empty list.
Say out loud: array children vs `HashMap` children (memory vs speed); the worst case of a pattern full of dots; how you would make `suggest` O(len(prefix)) (top-3 cached per node) and what that costs on `record`.
