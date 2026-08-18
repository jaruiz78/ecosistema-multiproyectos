package com.corp.proyectosubmarinevolcanomonitoring.infrastructure.adapter.in.web;

import com.corp.proyectosubmarinevolcanomonitoring.domain.model.VolcanicHydroacousticSeismicNode;
import com.corp.proyectosubmarinevolcanomonitoring.domain.port.in.ManageVolcanicHydroacousticSeismicNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectosubmarinevolcanomonitoring")
public class VolcanicHydroacousticSeismicNodeRestController {

    private final ManageVolcanicHydroacousticSeismicNodeUseCase useCase;

    public VolcanicHydroacousticSeismicNodeRestController(ManageVolcanicHydroacousticSeismicNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<VolcanicHydroacousticSeismicNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        VolcanicHydroacousticSeismicNode created = useCase.createVolcanicHydroacousticSeismicNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectosubmarinevolcanomonitoring/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolcanicHydroacousticSeismicNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findVolcanicHydroacousticSeismicNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
