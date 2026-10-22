# G - LLD sketch before coding (5 min)

- Classes and their single responsibility:
- Device state machine: which transitions are legal, what happens on a duplicate or out-of-order event:
- What is immutable, what is concurrent, who owns which lock (or which DB condition in a multi-pod setup):
- onDeviceEvent -> abort decision -> cancel flow, step by step:
- How stats() stays O(1):
- Edge cases I must handle (duplicate event, late event after abort, minimum sample, exactly at the threshold):
- How I will test it:
