package com.corp.proyectohyperloopintercitycorridor.infrastructure.adapter.in.web;

import com.corp.proyectohyperloopintercitycorridor.domain.model.HyperloopPodTelemetryTelemetryNode;
import com.corp.proyectohyperloopintercitycorridor.domain.port.in.ManageHyperloopPodTelemetryTelemetryNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectohyperloopintercitycorridor")
public class HyperloopPodTelemetryTelemetryNodeRestController {

    private final ManageHyperloopPodTelemetryTelemetryNodeUseCase useCase;

    public HyperloopPodTelemetryTelemetryNodeRestController(ManageHyperloopPodTelemetryTelemetryNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HyperloopPodTelemetryTelemetryNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HyperloopPodTelemetryTelemetryNode created = useCase.createHyperloopPodTelemetryTelemetryNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectohyperloopintercitycorridor/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HyperloopPodTelemetryTelemetryNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHyperloopPodTelemetryTelemetryNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
