package com.corp.proyectodeepseabenthicecosystems.infrastructure.adapter.in.web;

import com.corp.proyectodeepseabenthicecosystems.domain.model.HydrothermalVentBenthicZoneNode;
import com.corp.proyectodeepseabenthicecosystems.domain.port.in.ManageHydrothermalVentBenthicZoneNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodeepseabenthicecosystems")
public class HydrothermalVentBenthicZoneNodeRestController {

    private final ManageHydrothermalVentBenthicZoneNodeUseCase useCase;

    public HydrothermalVentBenthicZoneNodeRestController(ManageHydrothermalVentBenthicZoneNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HydrothermalVentBenthicZoneNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HydrothermalVentBenthicZoneNode created = useCase.createHydrothermalVentBenthicZoneNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodeepseabenthicecosystems/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HydrothermalVentBenthicZoneNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHydrothermalVentBenthicZoneNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
