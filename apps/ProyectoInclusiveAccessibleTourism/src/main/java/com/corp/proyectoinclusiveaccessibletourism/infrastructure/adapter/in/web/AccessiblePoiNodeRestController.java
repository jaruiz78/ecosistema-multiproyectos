package com.corp.proyectoinclusiveaccessibletourism.infrastructure.adapter.in.web;

import com.corp.proyectoinclusiveaccessibletourism.domain.model.AccessiblePoiNode;
import com.corp.proyectoinclusiveaccessibletourism.domain.port.in.ManageAccessiblePoiNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoinclusiveaccessibletourism")
public class AccessiblePoiNodeRestController {

    private final ManageAccessiblePoiNodeUseCase useCase;

    public AccessiblePoiNodeRestController(ManageAccessiblePoiNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AccessiblePoiNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AccessiblePoiNode created = useCase.createAccessiblePoiNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoinclusiveaccessibletourism/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessiblePoiNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAccessiblePoiNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
