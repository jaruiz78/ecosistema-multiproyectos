package com.corp.proyectohyporheicwaterpurification.infrastructure.adapter.in.web;

import com.corp.proyectohyporheicwaterpurification.domain.model.HyporheicNitrateDenitrificationNode;
import com.corp.proyectohyporheicwaterpurification.domain.port.in.ManageHyporheicNitrateDenitrificationNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectohyporheicwaterpurification")
public class HyporheicNitrateDenitrificationNodeRestController {

    private final ManageHyporheicNitrateDenitrificationNodeUseCase useCase;

    public HyporheicNitrateDenitrificationNodeRestController(ManageHyporheicNitrateDenitrificationNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HyporheicNitrateDenitrificationNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HyporheicNitrateDenitrificationNode created = useCase.createHyporheicNitrateDenitrificationNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectohyporheicwaterpurification/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HyporheicNitrateDenitrificationNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHyporheicNitrateDenitrificationNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
