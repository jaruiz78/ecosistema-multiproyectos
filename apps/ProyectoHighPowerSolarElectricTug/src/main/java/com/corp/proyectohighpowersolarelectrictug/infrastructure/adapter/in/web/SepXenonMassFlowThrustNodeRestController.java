package com.corp.proyectohighpowersolarelectrictug.infrastructure.adapter.in.web;

import com.corp.proyectohighpowersolarelectrictug.domain.model.SepXenonMassFlowThrustNode;
import com.corp.proyectohighpowersolarelectrictug.domain.port.in.ManageSepXenonMassFlowThrustNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectohighpowersolarelectrictug")
public class SepXenonMassFlowThrustNodeRestController {

    private final ManageSepXenonMassFlowThrustNodeUseCase useCase;

    public SepXenonMassFlowThrustNodeRestController(ManageSepXenonMassFlowThrustNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SepXenonMassFlowThrustNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SepXenonMassFlowThrustNode created = useCase.createSepXenonMassFlowThrustNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectohighpowersolarelectrictug/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SepXenonMassFlowThrustNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSepXenonMassFlowThrustNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
