package com.corp.proyectoautonomousmaritimefreighter.infrastructure.adapter.in.web;

import com.corp.proyectoautonomousmaritimefreighter.domain.model.MassVesselCollisionAvoidanceNode;
import com.corp.proyectoautonomousmaritimefreighter.domain.port.in.ManageMassVesselCollisionAvoidanceNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoautonomousmaritimefreighter")
public class MassVesselCollisionAvoidanceNodeRestController {

    private final ManageMassVesselCollisionAvoidanceNodeUseCase useCase;

    public MassVesselCollisionAvoidanceNodeRestController(ManageMassVesselCollisionAvoidanceNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MassVesselCollisionAvoidanceNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MassVesselCollisionAvoidanceNode created = useCase.createMassVesselCollisionAvoidanceNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoautonomousmaritimefreighter/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MassVesselCollisionAvoidanceNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMassVesselCollisionAvoidanceNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
