> Code: `src/main/java/practice/d20261023/c1/TopKWords.java`. Test: `TopKWordsTest`.

# C1. Top K Frequent Words - hash map + bounded heap (20 min timer)

`topKFrequent(String[] words, int k)`: the `k` most frequent words, ordered by frequency descending; equal frequencies are ordered alphabetically ascending (plain `String.compareTo`).

`["i","love","leetcode","i","love","coding"], k=2 -> ["i","love"]`
`["the","day","is","sunny","the","the","the","sunny","is","is"], k=4 -> ["the","is","sunny","day"]`

Constraints: `n <= 500_000`, `1 <= k <= distinct words`. Target `O(n + d log k)`, not a full sort.

Say out loud: why the heap's top must be the WORST kept word (and what "worst" means with the tie rule); the full-sort and bucket-sort alternatives and their costs; how you would do it for a stream (count-min sketch + heap, or exact counts sharded by hash).
