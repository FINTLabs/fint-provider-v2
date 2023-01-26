package no.fintlabs.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.adapter.models.ResponseFintEvent;
import no.fintlabs.event.request.RequestEventService;
import no.fintlabs.event.response.ResponseEventService;
import no.vigoiks.resourceserver.security.FintJwtCorePrincipal;
import no.vigoiks.resourceserver.security.FintJwtEndUserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController()
public class EventController {

    private final RequestEventService requestEventService;

    private final ResponseEventService responseEventService;

    @GetMapping(value = {"/event/", "/event/{domainName}", "/event/{domainName}/{packageName}", "/event/{domainName}/{packageName}/{resourceName}"})
    public ResponseEntity<List<RequestFintEvent>> getEvents(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable(required = false) String domainName,
            @PathVariable(required = false) String packageName,
            @PathVariable(required = false) String resourceName,
            @RequestParam(defaultValue = "0") int size
    ) {
        String orgId = FintJwtCorePrincipal.from(jwt).getOrgId();
        if (StringUtils.isBlank(orgId)) {
            log.info("Orgid not found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                requestEventService.getEvents(orgId, domainName, packageName, resourceName, size)
        );
    }

    @PostMapping("/event")
    public ResponseEntity<Void> postEvent(@RequestBody ResponseFintEvent responseFintEvent) {
        responseEventService.handleEvent(responseFintEvent);
        return ResponseEntity.ok().build();
    }
}
