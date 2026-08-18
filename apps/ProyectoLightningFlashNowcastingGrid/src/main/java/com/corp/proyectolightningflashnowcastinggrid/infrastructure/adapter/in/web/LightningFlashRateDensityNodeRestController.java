package com.corp.proyectolightningflashnowcastinggrid.infrastructure.adapter.in.web;

import com.corp.proyectolightningflashnowcastinggrid.domain.model.LightningFlashRateDensityNode;
import com.corp.proyectolightningflashnowcastinggrid.domain.port.in.ManageLightningFlashRateDensityNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectolightningflashnowcastinggrid")
public class LightningFlashRateDensityNodeRestController {

    private final ManageLightningFlashRateDensityNodeUseCase useCase;

    public LightningFlashRateDensityNodeRestController(ManageLightningFlashRateDensityNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<LightningFlashRateDensityNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        LightningFlashRateDensityNode created = useCase.createLightningFlashRateDensityNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectolightningflashnowcastinggrid/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LightningFlashRateDensityNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findLightningFlashRateDensityNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
