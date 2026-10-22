## Principal-engineer closing round (10 min, speak out loud)

1. In E, ten pods each have a Caffeine cache and one SQS message evicts only one pod's entry. Give me two designs that fix the staleness and the cost of each. Then: FX is slow (p99 8 s) and Tomcat threads are exhausted. What do the timeout, circuit breaker and bulkhead each do, and in what order do they apply?
2. In F1, wave 2 hits 2.4% failures but only on hardware revision C, and the abort threshold is 2% overall. Do you abort, pause, or continue for the other revisions? Design the policy and the dashboard that lets an on-call decide in 2 minutes.
3. In D1, the height array is 10^10 readings from a sensor and memory is 1 MB. Which variant survives, what do you give up, and how do you shard it across machines (hint: each shard returns left max, right max and its own trapped water; merge boundaries)?
4. In your Enphase command/notification pipeline, how do you guarantee a firmware or device command is not applied twice when the charger reconnects and the cloud redelivers the job? Which exact field is your idempotency key and where is it checked (cloud, device, both)?
