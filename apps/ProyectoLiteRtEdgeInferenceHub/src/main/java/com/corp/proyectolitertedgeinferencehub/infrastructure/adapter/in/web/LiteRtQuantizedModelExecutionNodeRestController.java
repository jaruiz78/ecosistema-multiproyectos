package com.corp.proyectolitertedgeinferencehub.infrastructure.adapter.in.web;

import com.corp.proyectolitertedgeinferencehub.domain.model.LiteRtQuantizedModelExecutionNode;
import com.corp.proyectolitertedgeinferencehub.domain.port.in.ManageLiteRtQuantizedModelExecutionNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectolitertedgeinferencehub")
public class LiteRtQuantizedModelExecutionNodeRestController {

    private final ManageLiteRtQuantizedModelExecutionNodeUseCase useCase;

    public LiteRtQuantizedModelExecutionNodeRestController(ManageLiteRtQuantizedModelExecutionNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<LiteRtQuantizedModelExecutionNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        LiteRtQuantizedModelExecutionNode created = useCase.createLiteRtQuantizedModelExecutionNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectolitertedgeinferencehub/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LiteRtQuantizedModelExecutionNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findLiteRtQuantizedModelExecutionNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
