package com.corp.proyectoorbitaldebrislaserdeflector.infrastructure.adapter.in.web;

import com.corp.proyectoorbitaldebrislaserdeflector.domain.model.LaserAblationImpulseDeltaVToken;
import com.corp.proyectoorbitaldebrislaserdeflector.domain.port.in.ManageLaserAblationImpulseDeltaVTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoorbitaldebrislaserdeflector")
public class LaserAblationImpulseDeltaVTokenRestController {

    private final ManageLaserAblationImpulseDeltaVTokenUseCase useCase;

    public LaserAblationImpulseDeltaVTokenRestController(ManageLaserAblationImpulseDeltaVTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<LaserAblationImpulseDeltaVToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        LaserAblationImpulseDeltaVToken created = useCase.createLaserAblationImpulseDeltaVToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoorbitaldebrislaserdeflector/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaserAblationImpulseDeltaVToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findLaserAblationImpulseDeltaVTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
