> No code. Write your answer in `my-designs/F1-resumable-upload.md` (12 min out loud + 5 min self-review). No tests.

# F1. HLD: resumable large-file upload (a small feature of a big app)

Users of a vehicle-service app attach videos (up to 5 GB) from flaky mobile networks to a support case. Design the upload path.

Cover: requirements and numbers (uploads/day, file size, concurrency, retention); the API (`start`, `put chunk`, `status`, `complete`, `abort`) and idempotency of each; where the bytes go (direct to object storage with pre-signed URLs vs through your servers, and the trade-off); integrity (per-chunk and whole-file checksums); resume after a client or server crash; expiry and cleanup of abandoned uploads; virus scan / content-type check before the file becomes visible; limits and abuse (per-user quota, rate limits); metadata store and its consistency; scaling and cost; failure modes (S3 outage, duplicate complete, clock skew on expiry).

Then the follow-up: the app also needs resumable DOWNLOAD of the same file. What changes (HTTP range requests, CDN, signed URL expiry)?
