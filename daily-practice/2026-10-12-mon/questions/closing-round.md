## Principal-engineer closing round (10 min, speak out loud)

1. In E, ten pods all run the expiry job and the `device_command` table has 500M rows. What do you do (index on status + expiresAt, partition by time, archive, batch updates with limit)?
2. In F1, a customer reports being wrongly blocked at 40% of their limit. List the five things you would check (clock skew, shard imbalance, key collisions, NAT/IP sharing, stale config).
3. In D1, memory is limited to 1 MB and the stream is 10^10 numbers. What do you give up and what structure do you use?
4. In your Enphase command/notification pipeline, how do you guarantee a command is not executed twice when the charger reconnects and the cloud retries?
