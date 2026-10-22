> Write your answer in `my-designs/F2-live-charging-session.md` (15 min, then say it out loud).

## F) HLD — two small features of a big app (30 min, about 15 each)

Use this frame: requirements (functional, non-functional, scale numbers) -> API -> data model -> high-level diagram -> deep dive on the one hard part -> failure modes -> metrics.

### F2. Live charging-session status for a mobile app (Enphase-style)
User opens the app and sees live kWh, power and time remaining for the active session. 1M users may watch concurrently, chargers report every 10 s. Updates to the screen within 3 s of an event.

Think about: device -> cloud ingestion (IoT Core MQTT -> rule -> Kinesis/SQS), latest-state store (DynamoDB / cache), push to client (WebSocket vs SSE vs long polling vs periodic polling), connection fan-out and sticky routing, reconnect + missed-update recovery, battery cost on mobile, what happens when the charger goes offline (staleness indicator).
