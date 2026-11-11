# F2 - Resumable file upload service (HLD, 12 min out loud + 5 min self-review) - `my-designs/F2-resumable-upload.md`

A feature of a big app (documents/photos/video): users upload files from phones on flaky networks, up to 5 GB.

- Functional: start an upload, send chunks in any order, resume after a disconnect, finish -> file becomes available; cancel; per-user quota; virus/type scan before the file is visible; identical files are stored once.
- Non-functional: 100k concurrent uploads, 2 Tbit/s peak ingest; no byte is ever lost after it was acknowledged; the API servers must not proxy the bytes if avoidable.
- Cover: upload session API (create / status / put chunk / complete), pre-signed URLs and direct-to-object-store multipart, chunk integrity (checksums) and idempotent chunk retries, assembling and the commit point, dedup by content hash (and the privacy leak of "already exists" answers), scan pipeline and states, cleanup of abandoned sessions, quotas under concurrency, hot-key and cost considerations.
