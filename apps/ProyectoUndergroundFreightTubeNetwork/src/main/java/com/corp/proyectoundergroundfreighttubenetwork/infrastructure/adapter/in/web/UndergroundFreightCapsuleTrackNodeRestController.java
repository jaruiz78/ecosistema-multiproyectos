package com.corp.proyectoundergroundfreighttubenetwork.infrastructure.adapter.in.web;

import com.corp.proyectoundergroundfreighttubenetwork.domain.model.UndergroundFreightCapsuleTrackNode;
import com.corp.proyectoundergroundfreighttubenetwork.domain.port.in.ManageUndergroundFreightCapsuleTrackNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoundergroundfreighttubenetwork")
public class UndergroundFreightCapsuleTrackNodeRestController {

    private final ManageUndergroundFreightCapsuleTrackNodeUseCase useCase;

    public UndergroundFreightCapsuleTrackNodeRestController(ManageUndergroundFreightCapsuleTrackNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<UndergroundFreightCapsuleTrackNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        UndergroundFreightCapsuleTrackNode created = useCase.createUndergroundFreightCapsuleTrackNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoundergroundfreighttubenetwork/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UndergroundFreightCapsuleTrackNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findUndergroundFreightCapsuleTrackNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
