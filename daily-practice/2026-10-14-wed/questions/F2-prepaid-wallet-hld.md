> Write your answer in `my-designs/F2-prepaid-wallet.md`, then say it out loud. No code, no tests.

### F2. Prepaid wallet for public charging payments
A driver tops up a wallet with a card or UPI through a payment gateway, then each public charging session debits it. 2M wallets, 300 debits/sec at peak. Balance must never go negative, a top-up must credit exactly once even if the gateway sends its webhook three times, a debit must be exactly once even if the session service retries, and finance must reconcile every rupee against the gateway's settlement file.

Think about: double-entry ledger vs a mutable balance column, immutable entries with an idempotency key, current balance as a derived (cached) value, atomic debit with a conditional update, a hold/capture model for in-progress sessions, webhook signature check and replay safety, reconciliation job, refunds as compensating entries, hot wallet rows (fleet accounts).

Frame: requirements (functional, non-functional, scale numbers) -> API -> data model -> high-level diagram -> deep dive on the one hard part -> failure modes -> metrics.
