package practice.e;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO - write the three endpoints (see questions/E-support-case-api.md):
 *
 *  POST  /api/v1/cases          @Valid body CreateCaseRequest -> 201, Location: .../api/v1/cases/{id}, ETag: "<version>", body CaseView
 *  GET   /api/v1/cases          ?status= (optional) + Pageable (default size 20, sort createdAt DESC) -> 200, body PagedModel<CaseView>
 *  PATCH /api/v1/cases/{id}     header If-Match: "<version>" (missing/unparsable -> PreconditionRequiredException -> 428),
 *                               @Valid body UpdateCaseRequest -> 200, ETag: "<new version>", body CaseView
 */
@RestController
@RequestMapping("/api/v1/cases")
public class CaseController {

    private final CaseService service;

    public CaseController(CaseService service) { this.service = service; }

    // TODO: endpoints here
}
