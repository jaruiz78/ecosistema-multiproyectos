package com.corp.proyectoliquidmetalsoftelectronics.infrastructure.adapter.in.web;

import com.corp.proyectoliquidmetalsoftelectronics.domain.model.LiquidMetalConductorTraceBatch;
import com.corp.proyectoliquidmetalsoftelectronics.domain.port.in.ManageLiquidMetalConductorTraceBatchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoliquidmetalsoftelectronics")
public class LiquidMetalConductorTraceBatchRestController {

    private final ManageLiquidMetalConductorTraceBatchUseCase useCase;

    public LiquidMetalConductorTraceBatchRestController(ManageLiquidMetalConductorTraceBatchUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<LiquidMetalConductorTraceBatch> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        LiquidMetalConductorTraceBatch created = useCase.createLiquidMetalConductorTraceBatch(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoliquidmetalsoftelectronics/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LiquidMetalConductorTraceBatch> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findLiquidMetalConductorTraceBatchById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
