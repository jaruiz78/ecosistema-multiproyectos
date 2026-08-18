package com.corp.proyectoautonomousverticalfarming.infrastructure.adapter.in.web;

import com.corp.proyectoautonomousverticalfarming.domain.model.VerticalFarmCanopyGrowthNode;
import com.corp.proyectoautonomousverticalfarming.domain.port.in.ManageVerticalFarmCanopyGrowthNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoautonomousverticalfarming")
public class VerticalFarmCanopyGrowthNodeRestController {

    private final ManageVerticalFarmCanopyGrowthNodeUseCase useCase;

    public VerticalFarmCanopyGrowthNodeRestController(ManageVerticalFarmCanopyGrowthNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<VerticalFarmCanopyGrowthNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        VerticalFarmCanopyGrowthNode created = useCase.createVerticalFarmCanopyGrowthNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoautonomousverticalfarming/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VerticalFarmCanopyGrowthNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findVerticalFarmCanopyGrowthNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
