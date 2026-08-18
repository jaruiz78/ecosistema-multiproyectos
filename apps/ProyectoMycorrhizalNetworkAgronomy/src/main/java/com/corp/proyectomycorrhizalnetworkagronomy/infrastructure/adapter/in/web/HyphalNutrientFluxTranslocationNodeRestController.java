package com.corp.proyectomycorrhizalnetworkagronomy.infrastructure.adapter.in.web;

import com.corp.proyectomycorrhizalnetworkagronomy.domain.model.HyphalNutrientFluxTranslocationNode;
import com.corp.proyectomycorrhizalnetworkagronomy.domain.port.in.ManageHyphalNutrientFluxTranslocationNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomycorrhizalnetworkagronomy")
public class HyphalNutrientFluxTranslocationNodeRestController {

    private final ManageHyphalNutrientFluxTranslocationNodeUseCase useCase;

    public HyphalNutrientFluxTranslocationNodeRestController(ManageHyphalNutrientFluxTranslocationNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HyphalNutrientFluxTranslocationNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HyphalNutrientFluxTranslocationNode created = useCase.createHyphalNutrientFluxTranslocationNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomycorrhizalnetworkagronomy/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HyphalNutrientFluxTranslocationNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHyphalNutrientFluxTranslocationNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
