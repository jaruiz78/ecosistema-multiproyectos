package com.corp.proyectoomniplanetaryhypertwin.infrastructure.adapter.in.web;

import com.corp.proyectoomniplanetaryhypertwin.domain.model.HyperPlanetaryTensorNexusNode;
import com.corp.proyectoomniplanetaryhypertwin.domain.port.in.ManageHyperPlanetaryTensorNexusNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoomniplanetaryhypertwin")
public class HyperPlanetaryTensorNexusNodeRestController {

    private final ManageHyperPlanetaryTensorNexusNodeUseCase useCase;

    public HyperPlanetaryTensorNexusNodeRestController(ManageHyperPlanetaryTensorNexusNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HyperPlanetaryTensorNexusNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HyperPlanetaryTensorNexusNode created = useCase.createHyperPlanetaryTensorNexusNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoomniplanetaryhypertwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HyperPlanetaryTensorNexusNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHyperPlanetaryTensorNexusNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
