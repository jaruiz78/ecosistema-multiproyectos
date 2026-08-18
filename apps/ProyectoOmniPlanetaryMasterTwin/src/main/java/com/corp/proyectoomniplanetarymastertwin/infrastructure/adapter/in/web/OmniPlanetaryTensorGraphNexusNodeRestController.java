package com.corp.proyectoomniplanetarymastertwin.infrastructure.adapter.in.web;

import com.corp.proyectoomniplanetarymastertwin.domain.model.OmniPlanetaryTensorGraphNexusNode;
import com.corp.proyectoomniplanetarymastertwin.domain.port.in.ManageOmniPlanetaryTensorGraphNexusNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoomniplanetarymastertwin")
public class OmniPlanetaryTensorGraphNexusNodeRestController {

    private final ManageOmniPlanetaryTensorGraphNexusNodeUseCase useCase;

    public OmniPlanetaryTensorGraphNexusNodeRestController(ManageOmniPlanetaryTensorGraphNexusNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<OmniPlanetaryTensorGraphNexusNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        OmniPlanetaryTensorGraphNexusNode created = useCase.createOmniPlanetaryTensorGraphNexusNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoomniplanetarymastertwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OmniPlanetaryTensorGraphNexusNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findOmniPlanetaryTensorGraphNexusNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
