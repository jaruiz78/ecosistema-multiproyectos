package com.corp.proyectospacedebrislasermitigation.infrastructure.adapter.in.web;

import com.corp.proyectospacedebrislasermitigation.domain.model.SpaceDebrisConjunctionTrackToken;
import com.corp.proyectospacedebrislasermitigation.domain.port.in.ManageSpaceDebrisConjunctionTrackTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectospacedebrislasermitigation")
public class SpaceDebrisConjunctionTrackTokenRestController {

    private final ManageSpaceDebrisConjunctionTrackTokenUseCase useCase;

    public SpaceDebrisConjunctionTrackTokenRestController(ManageSpaceDebrisConjunctionTrackTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SpaceDebrisConjunctionTrackToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SpaceDebrisConjunctionTrackToken created = useCase.createSpaceDebrisConjunctionTrackToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectospacedebrislasermitigation/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceDebrisConjunctionTrackToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSpaceDebrisConjunctionTrackTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
