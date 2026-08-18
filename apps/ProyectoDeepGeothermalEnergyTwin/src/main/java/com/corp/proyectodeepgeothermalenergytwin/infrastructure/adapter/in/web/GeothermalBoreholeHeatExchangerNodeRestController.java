package com.corp.proyectodeepgeothermalenergytwin.infrastructure.adapter.in.web;

import com.corp.proyectodeepgeothermalenergytwin.domain.model.GeothermalBoreholeHeatExchangerNode;
import com.corp.proyectodeepgeothermalenergytwin.domain.port.in.ManageGeothermalBoreholeHeatExchangerNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodeepgeothermalenergytwin")
public class GeothermalBoreholeHeatExchangerNodeRestController {

    private final ManageGeothermalBoreholeHeatExchangerNodeUseCase useCase;

    public GeothermalBoreholeHeatExchangerNodeRestController(ManageGeothermalBoreholeHeatExchangerNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<GeothermalBoreholeHeatExchangerNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        GeothermalBoreholeHeatExchangerNode created = useCase.createGeothermalBoreholeHeatExchangerNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodeepgeothermalenergytwin/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeothermalBoreholeHeatExchangerNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findGeothermalBoreholeHeatExchangerNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
