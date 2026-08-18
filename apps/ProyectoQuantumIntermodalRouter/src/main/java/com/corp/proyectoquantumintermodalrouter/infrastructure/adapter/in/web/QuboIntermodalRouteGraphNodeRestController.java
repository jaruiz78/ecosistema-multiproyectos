package com.corp.proyectoquantumintermodalrouter.infrastructure.adapter.in.web;

import com.corp.proyectoquantumintermodalrouter.domain.model.QuboIntermodalRouteGraphNode;
import com.corp.proyectoquantumintermodalrouter.domain.port.in.ManageQuboIntermodalRouteGraphNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantumintermodalrouter")
public class QuboIntermodalRouteGraphNodeRestController {

    private final ManageQuboIntermodalRouteGraphNodeUseCase useCase;

    public QuboIntermodalRouteGraphNodeRestController(ManageQuboIntermodalRouteGraphNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<QuboIntermodalRouteGraphNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        QuboIntermodalRouteGraphNode created = useCase.createQuboIntermodalRouteGraphNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantumintermodalrouter/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuboIntermodalRouteGraphNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findQuboIntermodalRouteGraphNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
