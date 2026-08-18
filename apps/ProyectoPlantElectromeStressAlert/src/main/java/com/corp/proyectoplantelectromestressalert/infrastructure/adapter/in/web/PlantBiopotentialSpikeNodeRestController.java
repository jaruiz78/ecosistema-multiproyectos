package com.corp.proyectoplantelectromestressalert.infrastructure.adapter.in.web;

import com.corp.proyectoplantelectromestressalert.domain.model.PlantBiopotentialSpikeNode;
import com.corp.proyectoplantelectromestressalert.domain.port.in.ManagePlantBiopotentialSpikeNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoplantelectromestressalert")
public class PlantBiopotentialSpikeNodeRestController {

    private final ManagePlantBiopotentialSpikeNodeUseCase useCase;

    public PlantBiopotentialSpikeNodeRestController(ManagePlantBiopotentialSpikeNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<PlantBiopotentialSpikeNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        PlantBiopotentialSpikeNode created = useCase.createPlantBiopotentialSpikeNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoplantelectromestressalert/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantBiopotentialSpikeNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findPlantBiopotentialSpikeNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
