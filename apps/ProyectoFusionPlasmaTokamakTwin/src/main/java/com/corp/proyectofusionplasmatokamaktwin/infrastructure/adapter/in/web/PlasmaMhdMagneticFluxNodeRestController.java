package com.corp.proyectofusionplasmatokamaktwin.infrastructure.adapter.in.web;

import com.corp.proyectofusionplasmatokamaktwin.domain.model.PlasmaMhdMagneticFluxNode;
import com.corp.proyectofusionplasmatokamaktwin.domain.port.in.ManagePlasmaMhdMagneticFluxNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectofusionplasmatokamaktwin")
public class PlasmaMhdMagneticFluxNodeRestController {

    private final ManagePlasmaMhdMagneticFluxNodeUseCase useCase;

    public PlasmaMhdMagneticFluxNodeRestController(ManagePlasmaMhdMagneticFluxNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PlasmaMhdMagneticFluxNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PlasmaMhdMagneticFluxNode created = useCase.createPlasmaMhdMagneticFluxNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectofusionplasmatokamaktwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlasmaMhdMagneticFluxNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPlasmaMhdMagneticFluxNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
