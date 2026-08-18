package com.corp.proyectomicrosatkabandsarconstellation.infrastructure.adapter.in.web;

import com.corp.proyectomicrosatkabandsarconstellation.domain.model.KaBandSarImageResolutionGridNode;
import com.corp.proyectomicrosatkabandsarconstellation.domain.port.in.ManageKaBandSarImageResolutionGridNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomicrosatkabandsarconstellation")
public class KaBandSarImageResolutionGridNodeRestController {

    private final ManageKaBandSarImageResolutionGridNodeUseCase useCase;

    public KaBandSarImageResolutionGridNodeRestController(ManageKaBandSarImageResolutionGridNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<KaBandSarImageResolutionGridNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        KaBandSarImageResolutionGridNode created = useCase.createKaBandSarImageResolutionGridNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomicrosatkabandsarconstellation/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KaBandSarImageResolutionGridNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findKaBandSarImageResolutionGridNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
