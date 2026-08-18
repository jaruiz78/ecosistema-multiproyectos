package com.corp.proyectostratospherictelecomballoons.infrastructure.adapter.in.web;

import com.corp.proyectostratospherictelecomballoons.domain.model.StratosphericStationKeepingTrajectoryNode;
import com.corp.proyectostratospherictelecomballoons.domain.port.in.ManageStratosphericStationKeepingTrajectoryNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectostratospherictelecomballoons")
public class StratosphericStationKeepingTrajectoryNodeRestController {

    private final ManageStratosphericStationKeepingTrajectoryNodeUseCase useCase;

    public StratosphericStationKeepingTrajectoryNodeRestController(ManageStratosphericStationKeepingTrajectoryNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<StratosphericStationKeepingTrajectoryNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        StratosphericStationKeepingTrajectoryNode created = useCase.createStratosphericStationKeepingTrajectoryNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectostratospherictelecomballoons/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StratosphericStationKeepingTrajectoryNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findStratosphericStationKeepingTrajectoryNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
