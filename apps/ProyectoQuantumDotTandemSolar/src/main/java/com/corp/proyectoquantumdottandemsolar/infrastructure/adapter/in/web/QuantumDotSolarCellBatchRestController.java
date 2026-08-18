package com.corp.proyectoquantumdottandemsolar.infrastructure.adapter.in.web;

import com.corp.proyectoquantumdottandemsolar.domain.model.QuantumDotSolarCellBatch;
import com.corp.proyectoquantumdottandemsolar.domain.port.in.ManageQuantumDotSolarCellBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantumdottandemsolar")
public class QuantumDotSolarCellBatchRestController {

    private final ManageQuantumDotSolarCellBatchUseCase useCase;

    public QuantumDotSolarCellBatchRestController(ManageQuantumDotSolarCellBatchUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<QuantumDotSolarCellBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        QuantumDotSolarCellBatch created = useCase.createQuantumDotSolarCellBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantumdottandemsolar/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuantumDotSolarCellBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findQuantumDotSolarCellBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
