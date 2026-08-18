package com.corp.proyectodiamondnvmagnetometry.infrastructure.adapter.in.web;

import com.corp.proyectodiamondnvmagnetometry.domain.model.DiamondNvMagnetometerNode;
import com.corp.proyectodiamondnvmagnetometry.domain.port.in.ManageDiamondNvMagnetometerNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodiamondnvmagnetometry")
public class DiamondNvMagnetometerNodeRestController {

    private final ManageDiamondNvMagnetometerNodeUseCase useCase;

    public DiamondNvMagnetometerNodeRestController(ManageDiamondNvMagnetometerNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DiamondNvMagnetometerNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DiamondNvMagnetometerNode created = useCase.createDiamondNvMagnetometerNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodiamondnvmagnetometry/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiamondNvMagnetometerNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDiamondNvMagnetometerNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
