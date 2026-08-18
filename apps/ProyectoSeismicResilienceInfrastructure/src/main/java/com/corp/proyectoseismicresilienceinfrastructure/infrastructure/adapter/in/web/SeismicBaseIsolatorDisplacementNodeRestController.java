package com.corp.proyectoseismicresilienceinfrastructure.infrastructure.adapter.in.web;

import com.corp.proyectoseismicresilienceinfrastructure.domain.model.SeismicBaseIsolatorDisplacementNode;
import com.corp.proyectoseismicresilienceinfrastructure.domain.port.in.ManageSeismicBaseIsolatorDisplacementNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoseismicresilienceinfrastructure")
public class SeismicBaseIsolatorDisplacementNodeRestController {

    private final ManageSeismicBaseIsolatorDisplacementNodeUseCase useCase;

    public SeismicBaseIsolatorDisplacementNodeRestController(ManageSeismicBaseIsolatorDisplacementNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SeismicBaseIsolatorDisplacementNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SeismicBaseIsolatorDisplacementNode created = useCase.createSeismicBaseIsolatorDisplacementNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoseismicresilienceinfrastructure/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeismicBaseIsolatorDisplacementNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSeismicBaseIsolatorDisplacementNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
