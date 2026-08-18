package com.corp.proyectoatmosphericwaterharvesting.infrastructure.adapter.in.web;

import com.corp.proyectoatmosphericwaterharvesting.domain.model.MofWaterAdsorptionChamberNode;
import com.corp.proyectoatmosphericwaterharvesting.domain.port.in.ManageMofWaterAdsorptionChamberNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoatmosphericwaterharvesting")
public class MofWaterAdsorptionChamberNodeRestController {

    private final ManageMofWaterAdsorptionChamberNodeUseCase useCase;

    public MofWaterAdsorptionChamberNodeRestController(ManageMofWaterAdsorptionChamberNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MofWaterAdsorptionChamberNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MofWaterAdsorptionChamberNode created = useCase.createMofWaterAdsorptionChamberNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoatmosphericwaterharvesting/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MofWaterAdsorptionChamberNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMofWaterAdsorptionChamberNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
