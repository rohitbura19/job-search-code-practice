# C2 - Trie (20 min) - `c2/Trie.java`, `c2/TrieProblems.java`

1. `Trie`: `insert`, `search`, `startsWith`, `countWithPrefix` (counts duplicates), `erase` (removes one occurrence, false if absent). Lowercase a-z, empty word allowed.
2. `replaceWords(roots, sentence)`: replace each word with the shortest root that is its prefix. `["cat","bat","rat"]`, `"the cattle was rattled by the battery"` -> `"the cat was rat by the bat"`.
3. `longestBuildableWord(words)`: longest word all of whose prefixes (down to length 1) are also in `words`; ties lexicographically smallest; `""` if none. The empty string never qualifies. `["a","banana","app","appl","ap","apply","apple"]` -> `"apple"`.
