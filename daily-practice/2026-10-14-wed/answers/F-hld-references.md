# Answers: F1 + F2 (HLD references) and principal-level discussions

(Spoilers. Open only after your attempt.)

## Reference — F1
```
App --PUT /chargers/{id}/schedule (rules, zone, expectedVersion)--> Schedule Service --> schedule table (version++)
     --> update IoT shadow "desired" = {version, zone, rules[]}     (AWS IoT Core, MQTT)
Charger (connected or later) <-- shadow delta -- applies rules locally, stores them in flash
Charger --> shadow "reported" = {version, appliedAt}  --> IoT rule --> SQS --> Status Consumer --> schedule_state(chargerId, appliedVersion)
Charger evaluates schedule itself using its local clock + zone rules (no cloud tick)
App shows: desiredVersion == reportedVersion ? "Applied" : "Pending (charger offline?)"
```
- **Evaluate on the device.** A cloud scheduler that sends "start" and "stop" commands to 1M devices at 23:00 is a thundering herd and fails entirely when the charger is offline. Instead the cloud delivers the rules once and the charger decides on its own.
- **Store local rules, not instants.** "23:00 to 06:00 Europe/Berlin" is the user's intent; converting to UTC breaks twice a year. Store `{days, start, end, ianaZone}`; the device ships tz rules (or you ship the next few transition instants with the document and refresh them periodically).
- **Desired vs reported state.** The cloud only writes the desired document with a monotonically increasing `version`. The device acks by reporting the version it applied. Edits use optimistic concurrency (`expectedVersion`) so two phones cannot overwrite each other silently. Offline device: the shadow keeps the latest desired state and delivers a delta on reconnect; intermediate versions are skipped, only the latest matters.
- **Windows crossing midnight** belong to the day they START on ("Mon 23:00 to 06:00" ends Tuesday 06:00). The evaluator must check today's and yesterday's windows. DST: a window inside a spring-forward gap loses real time, a window across a fall-back repeat gains an hour; decide and document the rule (use the wall-clock as the user means it).
- **Conflicts**: model as constraints that are AND-ed: user schedule allows, tariff says not peak, grid event not curtailing. Anything saying "no" wins; safety (curtailment) outranks user comfort. Return the reason so the app can explain "paused by utility event".
- **Herd control**: 1M devices reporting at exactly 23:00 floods ingestion. Devices add random jitter (0 to 120 s) to any report triggered by a schedule edge, and report only on change, not on every tick.
- **Clock drift**: devices sync NTP; if the clock is untrusted (no sync since boot) the device falls back to a safe default (charge allowed, or last known) and reports it.
- **Metrics**: percentage of chargers whose reported version equals desired within 5 minutes, p95 time-to-apply, devices stuck pending > 24 h, schedule-edge report burst rate, count of DST-day support tickets.

## Reference — F2
```
Gateway webhook --> Wallet API: verify signature --> insert ledger entries (idempotent on gateway_txn_id) --> update balance cache
Session Service --> POST /wallets/{id}/holds (idempotencyKey = sessionId) --> capture at session end (actual cost) / release
Ledger (append-only): entry(id, walletId, amount_minor, direction, type, idempotencyKey UNIQUE, refType, refId, createdAt)
Balance = SUM(entries) ; wallet.balance_cached updated in the same transaction
```
- **Append-only double-entry ledger.** Each business event writes balanced debit and credit entries (driver wallet vs operator revenue vs gateway clearing). Nothing is ever updated or deleted, so any balance can be reproduced and audited; a refund is a new compensating entry. A single `balance` column that is updated in place gives no audit trail and no way to explain a discrepancy.
- **Exactly-once credit**: unique constraint on `(type, gateway_txn_id)`. The webhook handler inserts; on a duplicate-key error it returns 200 (already processed). Always verify the HMAC signature and check the amount and currency against your own pending top-up record. Respond 2xx only after the commit.
- **No negative balance, atomically**: `UPDATE wallet SET balance = balance - :amt, version = version + 1 WHERE id = :id AND balance >= :amt`; zero rows updated means insufficient funds. This is correct and simple; the ledger entry insert is in the same transaction.
- **Hold/capture** for sessions: at session start place a hold of an estimated amount (reduces available balance, not the ledger balance); at session end capture the real cost (hold released, ledger debit written) or release it. Holds carry an expiry so a crashed session cannot lock funds forever.
- **Money**: integer minor units (paise) plus currency code. Never floating point.
- **Reconciliation**: nightly job joins ledger top-ups against the gateway settlement file by `gateway_txn_id`; differences become tickets: missing on our side (webhook lost -> fetch status via API and credit), missing on theirs (refund or fraud).
- **Hot wallet** (a fleet account with thousands of concurrent sessions): the single row serialises. Split into N sub-balances, or switch the fleet account to a post-paid invoiced model with credit limits.
- **Metrics**: webhook duplicate rate, unmatched reconciliation count and value, hold-to-capture lag, expired holds, wallet debit latency p99, ledger sum vs cached balance drift (should be zero).

## Principal-level discussion: make F1 more efficient
(The sheet gives these as questions only, with no written reference. Compare with the F1 reference above: template + overrides with a version pointer, rate-limited fan-out, tz data as separately versioned config with a fallback for old firmware, schedule as a pure function of (rules, now), supersede by version so only the latest desired state is delivered, jittered and batched status writes with latest-state-only storage.)
