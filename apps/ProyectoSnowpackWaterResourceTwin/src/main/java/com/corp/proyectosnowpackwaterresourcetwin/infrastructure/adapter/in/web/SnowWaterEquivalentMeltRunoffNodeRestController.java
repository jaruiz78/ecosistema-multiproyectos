package com.corp.proyectosnowpackwaterresourcetwin.infrastructure.adapter.in.web;

import com.corp.proyectosnowpackwaterresourcetwin.domain.model.SnowWaterEquivalentMeltRunoffNode;
import com.corp.proyectosnowpackwaterresourcetwin.domain.port.in.ManageSnowWaterEquivalentMeltRunoffNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosnowpackwaterresourcetwin")
public class SnowWaterEquivalentMeltRunoffNodeRestController {

    private final ManageSnowWaterEquivalentMeltRunoffNodeUseCase useCase;

    public SnowWaterEquivalentMeltRunoffNodeRestController(ManageSnowWaterEquivalentMeltRunoffNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SnowWaterEquivalentMeltRunoffNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SnowWaterEquivalentMeltRunoffNode created = useCase.createSnowWaterEquivalentMeltRunoffNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosnowpackwaterresourcetwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SnowWaterEquivalentMeltRunoffNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSnowWaterEquivalentMeltRunoffNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
