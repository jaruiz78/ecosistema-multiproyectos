package com.corp.proyectocgraspatialaccelerator.infrastructure.adapter.in.web;

import com.corp.proyectocgraspatialaccelerator.domain.model.CgraComputeMeshTileNode;
import com.corp.proyectocgraspatialaccelerator.domain.port.in.ManageCgraComputeMeshTileNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocgraspatialaccelerator")
public class CgraComputeMeshTileNodeRestController {

    private final ManageCgraComputeMeshTileNodeUseCase useCase;

    public CgraComputeMeshTileNodeRestController(ManageCgraComputeMeshTileNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CgraComputeMeshTileNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CgraComputeMeshTileNode created = useCase.createCgraComputeMeshTileNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocgraspatialaccelerator/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CgraComputeMeshTileNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCgraComputeMeshTileNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
