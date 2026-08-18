package com.corp.proyectocislunarspacelogistics.infrastructure.adapter.in.web;

import com.corp.proyectocislunarspacelogistics.domain.model.LagrangeTrajectoryNode;
import com.corp.proyectocislunarspacelogistics.domain.port.in.ManageLagrangeTrajectoryNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocislunarspacelogistics")
public class LagrangeTrajectoryNodeRestController {

    private final ManageLagrangeTrajectoryNodeUseCase useCase;

    public LagrangeTrajectoryNodeRestController(ManageLagrangeTrajectoryNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<LagrangeTrajectoryNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        LagrangeTrajectoryNode created = useCase.createLagrangeTrajectoryNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocislunarspacelogistics/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LagrangeTrajectoryNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findLagrangeTrajectoryNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
