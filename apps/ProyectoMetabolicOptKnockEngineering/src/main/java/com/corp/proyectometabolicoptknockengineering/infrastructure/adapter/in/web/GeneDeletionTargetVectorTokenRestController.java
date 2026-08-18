package com.corp.proyectometabolicoptknockengineering.infrastructure.adapter.in.web;

import com.corp.proyectometabolicoptknockengineering.domain.model.GeneDeletionTargetVectorToken;
import com.corp.proyectometabolicoptknockengineering.domain.port.in.ManageGeneDeletionTargetVectorTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectometabolicoptknockengineering")
public class GeneDeletionTargetVectorTokenRestController {

    private final ManageGeneDeletionTargetVectorTokenUseCase useCase;

    public GeneDeletionTargetVectorTokenRestController(ManageGeneDeletionTargetVectorTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<GeneDeletionTargetVectorToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        GeneDeletionTargetVectorToken created = useCase.createGeneDeletionTargetVectorToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectometabolicoptknockengineering/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneDeletionTargetVectorToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findGeneDeletionTargetVectorTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
