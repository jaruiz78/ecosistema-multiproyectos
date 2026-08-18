package com.corp.proyectosmartdestinationdti.infrastructure.adapter.in.web;

import com.corp.proyectosmartdestinationdti.domain.model.DestinationCapacityZone;
import com.corp.proyectosmartdestinationdti.domain.port.in.ManageDestinationCapacityZoneUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosmartdestinationdti")
public class DestinationCapacityZoneRestController {

    private final ManageDestinationCapacityZoneUseCase useCase;

    public DestinationCapacityZoneRestController(ManageDestinationCapacityZoneUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DestinationCapacityZone> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DestinationCapacityZone created = useCase.createDestinationCapacityZone(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosmartdestinationdti/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationCapacityZone> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDestinationCapacityZoneById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
