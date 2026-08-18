package com.corp.proyectospaceweathergriddefense.infrastructure.adapter.in.web;

import com.corp.proyectospaceweathergriddefense.domain.model.GicTransformerNeutralCurrentAlertNode;
import com.corp.proyectospaceweathergriddefense.domain.port.in.ManageGicTransformerNeutralCurrentAlertNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectospaceweathergriddefense")
public class GicTransformerNeutralCurrentAlertNodeRestController {

    private final ManageGicTransformerNeutralCurrentAlertNodeUseCase useCase;

    public GicTransformerNeutralCurrentAlertNodeRestController(ManageGicTransformerNeutralCurrentAlertNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<GicTransformerNeutralCurrentAlertNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        GicTransformerNeutralCurrentAlertNode created = useCase.createGicTransformerNeutralCurrentAlertNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectospaceweathergriddefense/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GicTransformerNeutralCurrentAlertNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findGicTransformerNeutralCurrentAlertNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
