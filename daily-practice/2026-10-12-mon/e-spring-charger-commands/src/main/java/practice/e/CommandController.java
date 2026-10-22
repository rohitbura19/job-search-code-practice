package practice.e;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO - base path /api/v1/chargers (see questions/E-charger-commands.md):
 *
 *  POST /{chargerId}/commands   header Idempotency-Key (required), @Valid body CommandRequest
 *       -> 202 Accepted, Location: /api/v1/commands/{id}, body CommandResponse.from(service.create(...))
 *  GET  /{chargerId}/commands   ?page=0&size=20 (default size 20) -> Page<CommandResponse> built from service.history(chargerId, pageable)
 *
 * A missing header or an invalid body must be a 400 (Spring does that by itself once the annotations are right).
 */
@RestController
@RequestMapping("/api/v1/chargers")
public class CommandController {

    private final CommandService service;

    public CommandController(CommandService service) { this.service = service; }

    // TODO: endpoints here
}
