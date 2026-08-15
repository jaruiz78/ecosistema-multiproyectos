package com.corp.proyectosmartagrisupplychain.infrastructure.adapter.in.web;

import com.corp.proyectosmartagrisupplychain.domain.model.AgriSupplyTrack;
import com.corp.proyectosmartagrisupplychain.domain.port.in.ManageAgriSupplyTrackUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosmartagrisupplychain")
public class AgriSupplyTrackRestController {

    private final ManageAgriSupplyTrackUseCase useCase;

    public AgriSupplyTrackRestController(ManageAgriSupplyTrackUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AgriSupplyTrack> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AgriSupplyTrack created = useCase.createAgriSupplyTrack(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosmartagrisupplychain/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgriSupplyTrack> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAgriSupplyTrackById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
