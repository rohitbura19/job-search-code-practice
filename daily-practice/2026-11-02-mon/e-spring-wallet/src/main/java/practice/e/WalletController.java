package practice.e;

import org.springframework.http.ResponseEntity;

// TODO: REST controller under /api.
//  POST /api/accounts            validated body -> 201 + Location /api/accounts/{id} + AccountView
//  GET  /api/accounts/{id}       -> AccountView
//  POST /api/transfers           header Idempotency-Key (required, not blank, max 64 chars -> 400), validated body
//                                -> 201 for a new transfer, 200 when replayed (TransferOutcome.created() == false)
public class WalletController {
    public WalletController(WalletService service) { throw new UnsupportedOperationException("TODO"); }

    public ResponseEntity<AccountView> open(AccountRequest req) { throw new UnsupportedOperationException("TODO"); }

    public AccountView get(Long id) { throw new UnsupportedOperationException("TODO"); }

    public ResponseEntity<TransferView> transfer(String key, TransferRequest req) { throw new UnsupportedOperationException("TODO"); }
}
