package com.corp.proyectocryoagrifoodlogistics.infrastructure.adapter.in.web;

import com.corp.proyectocryoagrifoodlogistics.domain.model.CryogenicTelemetryBatchNode;
import com.corp.proyectocryoagrifoodlogistics.domain.port.in.ManageCryogenicTelemetryBatchNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocryoagrifoodlogistics")
public class CryogenicTelemetryBatchNodeRestController {

    private final ManageCryogenicTelemetryBatchNodeUseCase useCase;

    public CryogenicTelemetryBatchNodeRestController(ManageCryogenicTelemetryBatchNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CryogenicTelemetryBatchNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CryogenicTelemetryBatchNode created = useCase.createCryogenicTelemetryBatchNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocryoagrifoodlogistics/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CryogenicTelemetryBatchNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCryogenicTelemetryBatchNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
