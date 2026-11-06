# F2 - HLD: online presence and last seen (12 min out loud + 5 min review)
Feature of a chat app with 100M DAU: show a green dot / "last seen 5 min ago" for friends and group members.
Requirements: updates visible within ~10 s, approximate is fine, 5M concurrent connections, a user may open the app on 3 devices, privacy setting "hide last seen".
Cover: heartbeat vs connection-based detection, TTL keys, fan-out (push to subscribers vs pull on open), multi-device merge, thundering herd after a gateway restart, data model and storage, privacy checks, what you drop under load.
