package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WalletController {
    private final WalletService service;

    public WalletController(WalletService service) { this.service = service; }

    @PostMapping("/accounts")
    public ResponseEntity<AccountView> open(@Valid @RequestBody AccountRequest req) {
        AccountView v = service.createAccount(req);
        return ResponseEntity.created(URI.create("/api/accounts/" + v.id())).body(v);
    }

    @GetMapping("/accounts/{id}")
    public AccountView get(@PathVariable Long id) { return service.getAccount(id); }

    /** 201 for a new transfer, 200 when the Idempotency-Key was seen before. Header: required, not blank, max 64 chars -> 400. */
    @PostMapping("/transfers")
    public ResponseEntity<TransferView> transfer(
            @RequestHeader("Idempotency-Key") @NotBlank @Size(max = 64) String key,
            @Valid @RequestBody TransferRequest req) {
        TransferOutcome out = service.transfer(key, req);
        return ResponseEntity.status(out.created() ? HttpStatus.CREATED : HttpStatus.OK).body(out.view());
    }
}
