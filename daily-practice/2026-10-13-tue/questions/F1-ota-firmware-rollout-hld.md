> Write your answer in `my-designs/F1-ota-rollout.md`, then say it out loud. No tests.

## F) HLD — two small features of a big app (30 min, about 15 each)

Use this frame: requirements (functional, non-functional, scale numbers) -> API -> data model -> high-level diagram -> deep dive on the one hard part -> failure modes -> metrics.

### F1. Firmware OTA rollout for 500k EV chargers (Enphase-style)
Release a 40 MB firmware image to 500k chargers worldwide. Rollout in waves (1% canary, 10%, 50%, 100%) with a soak time between waves. Never update a charger mid-session. Auto-abort if more than 2% of a wave fails. Chargers can be offline for days, may have different hardware revisions, and the download must be resumable. The image must be authentic (signed). Operators need live progress per wave.

Think about: control plane vs data plane (command via MQTT, bytes via CDN), device state machine, eligibility rules (hw revision, current version, not charging), bandwidth control (500k × 40 MB is 20 TB; do not send it all at once), signed image + A/B partitions + rollback, retry for offline devices, status ingestion (IoT Core -> SQS), aggregating counters without a hot partition, idempotent job ids.

### Principal-level discussion: make F1 more efficient
1. **Delta updates**: ship a binary diff (bsdiff) between versions so a 40 MB image becomes about 4 MB. What do you lose (must know the exact source version, more combinations to build and test, fallback to full image)?
2. **Push vs pull**: instead of per-device MQTT notifications, let chargers poll an "assigned version" with a jittered interval (or read their IoT shadow). Which one scales better, which gives you better control of the rollout rate, and what is the battery/network cost for devices?
3. **Blast radius**: canary by hardware revision and by region, never by "random 1%" alone. How do you pick a canary set that represents the long tail of devices? What health signal beyond "install succeeded" do you watch (fault rate, charging success rate, reconnect rate for 24 h)?
4. **Thundering herd**: 50% wave starts and 250k devices fetch at once. Per-site caps, regional waves following local night time, jitter, and CDN pre-warming. What does 10x do to your status-ingestion SQS queue and DynamoDB write capacity?
5. **What if the abort decision is wrong or late?** Pause vs abort vs roll back, who can override, and how do you stop devices already downloading?
