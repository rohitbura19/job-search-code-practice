# D1 - Reconstruct Itinerary (20 min) - `d1/ReconstructItinerary.java`

`tickets[i] = [from, to]`. Rebuild the itinerary starting at `"JFK"` that uses every ticket exactly once; if several exist, return the lexicographically smallest (airport by airport). Duplicate tickets allowed; a valid itinerary exists.

`[[MUC,LHR],[JFK,MUC],[SFO,SJC],[LHR,SFO]]` -> `[JFK,MUC,LHR,SFO,SJC]`.
`[[JFK,SFO],[JFK,ATL],[SFO,ATL],[ATL,JFK],[ATL,SFO]]` -> `[JFK,ATL,JFK,SFO,ATL,SFO]`.
`[[JFK,A],[JFK,B],[B,JFK]]` -> `[JFK,B,JFK,A]` (the smallest first hop is a dead end).

Up to 300 000 tickets: avoid deep recursion. Target O(E log E).
