package com.corp.proyectoestuarinesalinityintrusiontwin.infrastructure.adapter.in.web;

import com.corp.proyectoestuarinesalinityintrusiontwin.domain.model.EstuarineSalinityIsohalineDistanceNode;
import com.corp.proyectoestuarinesalinityintrusiontwin.domain.port.in.ManageEstuarineSalinityIsohalineDistanceNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoestuarinesalinityintrusiontwin")
public class EstuarineSalinityIsohalineDistanceNodeRestController {

    private final ManageEstuarineSalinityIsohalineDistanceNodeUseCase useCase;

    public EstuarineSalinityIsohalineDistanceNodeRestController(ManageEstuarineSalinityIsohalineDistanceNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<EstuarineSalinityIsohalineDistanceNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        EstuarineSalinityIsohalineDistanceNode created = useCase.createEstuarineSalinityIsohalineDistanceNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoestuarinesalinityintrusiontwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstuarineSalinityIsohalineDistanceNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findEstuarineSalinityIsohalineDistanceNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
