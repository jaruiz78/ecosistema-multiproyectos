package com.corp.proyectodroneairspace.infrastructure.adapter.in.web;

import com.corp.proyectodroneairspace.domain.model.DroneRoute;
import com.corp.proyectodroneairspace.domain.port.in.ManageDroneRouteUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodroneairspace")
public class DroneRouteRestController {

    private final ManageDroneRouteUseCase useCase;

    public DroneRouteRestController(ManageDroneRouteUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DroneRoute> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DroneRoute created = useCase.createDroneRoute(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodroneairspace/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DroneRoute> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDroneRouteById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
