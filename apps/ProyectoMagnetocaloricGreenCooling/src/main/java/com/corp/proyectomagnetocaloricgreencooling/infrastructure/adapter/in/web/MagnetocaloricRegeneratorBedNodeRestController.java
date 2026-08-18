package com.corp.proyectomagnetocaloricgreencooling.infrastructure.adapter.in.web;

import com.corp.proyectomagnetocaloricgreencooling.domain.model.MagnetocaloricRegeneratorBedNode;
import com.corp.proyectomagnetocaloricgreencooling.domain.port.in.ManageMagnetocaloricRegeneratorBedNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectomagnetocaloricgreencooling")
public class MagnetocaloricRegeneratorBedNodeRestController {

    private final ManageMagnetocaloricRegeneratorBedNodeUseCase useCase;

    public MagnetocaloricRegeneratorBedNodeRestController(ManageMagnetocaloricRegeneratorBedNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<MagnetocaloricRegeneratorBedNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        MagnetocaloricRegeneratorBedNode created = useCase.createMagnetocaloricRegeneratorBedNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectomagnetocaloricgreencooling/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MagnetocaloricRegeneratorBedNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findMagnetocaloricRegeneratorBedNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
