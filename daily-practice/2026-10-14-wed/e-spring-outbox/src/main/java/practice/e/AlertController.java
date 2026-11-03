package practice.e;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO - POST /api/v1/alerts
 *  - validated body (AlertRequest), calls service.create
 *  - 201 Created, Location header ".../api/v1/alerts/{id}", body AlertResponse.from(alert)
 * (On paper: also @PreAuthorize("hasAuthority('SCOPE_alerts:write')") and the SecurityConfig; Spring Security is not on this classpath.)
 */
@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {
    private final AlertService service;

    public AlertController(AlertService service) { this.service = service; }

    // TODO: the endpoint
}
