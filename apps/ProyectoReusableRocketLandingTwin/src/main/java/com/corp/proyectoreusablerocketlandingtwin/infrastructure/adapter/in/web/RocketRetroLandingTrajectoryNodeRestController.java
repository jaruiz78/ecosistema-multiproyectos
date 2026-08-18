package com.corp.proyectoreusablerocketlandingtwin.infrastructure.adapter.in.web;

import com.corp.proyectoreusablerocketlandingtwin.domain.model.RocketRetroLandingTrajectoryNode;
import com.corp.proyectoreusablerocketlandingtwin.domain.port.in.ManageRocketRetroLandingTrajectoryNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoreusablerocketlandingtwin")
public class RocketRetroLandingTrajectoryNodeRestController {

    private final ManageRocketRetroLandingTrajectoryNodeUseCase useCase;

    public RocketRetroLandingTrajectoryNodeRestController(ManageRocketRetroLandingTrajectoryNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<RocketRetroLandingTrajectoryNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        RocketRetroLandingTrajectoryNode created = useCase.createRocketRetroLandingTrajectoryNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoreusablerocketlandingtwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RocketRetroLandingTrajectoryNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findRocketRetroLandingTrajectoryNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
