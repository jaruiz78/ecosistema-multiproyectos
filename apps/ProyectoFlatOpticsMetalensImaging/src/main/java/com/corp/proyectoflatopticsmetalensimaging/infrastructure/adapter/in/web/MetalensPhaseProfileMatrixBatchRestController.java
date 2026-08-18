package com.corp.proyectoflatopticsmetalensimaging.infrastructure.adapter.in.web;

import com.corp.proyectoflatopticsmetalensimaging.domain.model.MetalensPhaseProfileMatrixBatch;
import com.corp.proyectoflatopticsmetalensimaging.domain.port.in.ManageMetalensPhaseProfileMatrixBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoflatopticsmetalensimaging")
public class MetalensPhaseProfileMatrixBatchRestController {

    private final ManageMetalensPhaseProfileMatrixBatchUseCase useCase;

    public MetalensPhaseProfileMatrixBatchRestController(ManageMetalensPhaseProfileMatrixBatchUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MetalensPhaseProfileMatrixBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MetalensPhaseProfileMatrixBatch created = useCase.createMetalensPhaseProfileMatrixBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoflatopticsmetalensimaging/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetalensPhaseProfileMatrixBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMetalensPhaseProfileMatrixBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
