> Code: `src/main/java/practice/d20261013/g/` (given: `CampaignState`, `Device`, `Firmware`, `Wave`, `Stats`, `EligibilityRule`, `AbortPolicy`, `DeviceGateway`; write: `DeviceState`, `HwRevRule`, `NotChargingRule`, `Eligibility`, `FailureRateAbort`, `Campaign`, `StatusConsumer`). Sketch first in `my-designs/G-classes-sketch.md`. Test: `RolloutTest`. Two additions to the sheet: `stats()` must be O(1) (the sheet's own principal-level point) and `StatusConsumer` sends the cancel commands.

## G) LLD of the same question — Firmware rollout engine (25 min)

Design the in-process object model of the **Rollout Service** (F1). Requirements: campaigns with ordered waves; pluggable wave selection and eligibility rules; per-device state machine with idempotent transitions; pluggable abort policy; device communication hidden behind an interface; thread-safe; testable without real devices or real time.

Write the interfaces and classes, the "device reports FAILED" flow, and the abort decision. Then discuss patterns, concurrency and testing.
