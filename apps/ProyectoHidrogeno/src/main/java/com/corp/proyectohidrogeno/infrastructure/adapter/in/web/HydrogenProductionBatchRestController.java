package com.corp.proyectohidrogeno.infrastructure.adapter.in.web;

import com.corp.proyectohidrogeno.domain.model.HydrogenProductionBatch;
import com.corp.proyectohidrogeno.domain.port.in.ManageHydrogenProductionBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectohidrogeno")
public class HydrogenProductionBatchRestController {

    private final ManageHydrogenProductionBatchUseCase useCase;

    public HydrogenProductionBatchRestController(ManageHydrogenProductionBatchUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HydrogenProductionBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HydrogenProductionBatch created = useCase.createHydrogenProductionBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectohidrogeno/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HydrogenProductionBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHydrogenProductionBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
