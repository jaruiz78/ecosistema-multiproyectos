package com.corp.proyectocellfreeproteinbiomanufacturing.infrastructure.adapter.in.web;

import com.corp.proyectocellfreeproteinbiomanufacturing.domain.model.CfpsReactionYieldBatchToken;
import com.corp.proyectocellfreeproteinbiomanufacturing.domain.port.in.ManageCfpsReactionYieldBatchTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocellfreeproteinbiomanufacturing")
public class CfpsReactionYieldBatchTokenRestController {

    private final ManageCfpsReactionYieldBatchTokenUseCase useCase;

    public CfpsReactionYieldBatchTokenRestController(ManageCfpsReactionYieldBatchTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CfpsReactionYieldBatchToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CfpsReactionYieldBatchToken created = useCase.createCfpsReactionYieldBatchToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocellfreeproteinbiomanufacturing/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CfpsReactionYieldBatchToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCfpsReactionYieldBatchTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
