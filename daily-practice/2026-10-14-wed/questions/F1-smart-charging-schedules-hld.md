> Write your answer in `my-designs/F1-smart-charging-schedules.md`, then say it out loud. No code, no tests.

### F1. Smart-charging schedules for home EV chargers (Enphase-style)
Users set rules like "only charge Mon to Fri between 23:00 and 06:00" or "pause during the utility's peak-price hours". 1M chargers worldwide, up to 5 rules each, users edit schedules from the mobile app. The charger must obey the schedule even when its internet is down. A charger may be moved to a new time zone; daylight saving transitions must not cause missed or doubled windows. The cloud must know what schedule each charger currently runs, and the app must show "schedule applied" only when the charger has really accepted it.

Think about: where the schedule is evaluated (cloud tick vs the device itself), what you store (local time rule + IANA zone, never a UTC instant), versioned desired-state vs reported-state (AWS IoT device shadow), delivery to offline devices, DST gaps and overlaps, conflicts between user rule, utility tariff and grid-curtailment event, thundering herd when 1M devices wake at 23:00, clock drift on the device.

Frame: requirements (functional, non-functional, scale numbers) -> API -> data model -> high-level diagram -> deep dive on the one hard part -> failure modes -> metrics.

### Principal-level discussion: make F1 more efficient
1. **Delivery cost**: 1M devices x 5 rules is small data, but a mass edit (utility pushes a new peak-hours template to 400k chargers) is a write storm into the shadow service. How do you fan out with a rate limit, and how do you represent "template + overrides" so one template version change does not require rewriting 400k per-device documents?
2. **Time zone database drift**: governments change DST rules with a few weeks' notice. How does the charger learn new rules (ship a tz database as part of firmware? as a separately versioned config?), and what happens to chargers that stay on old firmware?
3. **Safety and correctness**: the charger loses power mid-window and boots at 02:00. It must resume the schedule evaluation from current time, not from a stored "I was in the window" flag. Why is the schedule a pure function of (rules, now) and why does that make it testable?
4. **Offline for weeks**: the user edited the schedule 5 times while offline. What does the charger see on reconnect, and how does a cloud-side "supersede" avoid replaying stale versions?
5. **What breaks at 10x chargers**: the status consumer and the `schedule_state` table. Where is the hot partition (hint: devices report at the same minute after a regional DST change), and what do you do (jitter, batching, TTL'd latest-state only)?
