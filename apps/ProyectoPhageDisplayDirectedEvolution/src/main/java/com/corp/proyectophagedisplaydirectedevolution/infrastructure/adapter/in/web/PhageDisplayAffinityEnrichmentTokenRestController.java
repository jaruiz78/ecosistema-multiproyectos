package com.corp.proyectophagedisplaydirectedevolution.infrastructure.adapter.in.web;

import com.corp.proyectophagedisplaydirectedevolution.domain.model.PhageDisplayAffinityEnrichmentToken;
import com.corp.proyectophagedisplaydirectedevolution.domain.port.in.ManagePhageDisplayAffinityEnrichmentTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectophagedisplaydirectedevolution")
public class PhageDisplayAffinityEnrichmentTokenRestController {

    private final ManagePhageDisplayAffinityEnrichmentTokenUseCase useCase;

    public PhageDisplayAffinityEnrichmentTokenRestController(ManagePhageDisplayAffinityEnrichmentTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PhageDisplayAffinityEnrichmentToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PhageDisplayAffinityEnrichmentToken created = useCase.createPhageDisplayAffinityEnrichmentToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectophagedisplaydirectedevolution/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhageDisplayAffinityEnrichmentToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPhageDisplayAffinityEnrichmentTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
