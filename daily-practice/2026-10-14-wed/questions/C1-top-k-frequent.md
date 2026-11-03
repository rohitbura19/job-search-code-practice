> Code: `src/main/java/practice/d20261014/c1/TopKFrequent.java`. Test: `TopKFrequentTest`.

### C1. Top K Frequent Elements (hash + bucket sort)
Return the `k` most frequent values. `[1,1,1,2,2,3], k=2 -> [1,2]`; `[1], k=1 -> [1]`. Must beat O(n log n).

Interviewer follow-up: compare the heap (O(n log k)), quickselect (average O(n)) and bucket (O(n)) approaches — which would you ship? The data is an endless stream of events and you want "top 10 most frequent chargers right now" in bounded memory. The data is spread over 100 machines — can each machine just send its local top K?
