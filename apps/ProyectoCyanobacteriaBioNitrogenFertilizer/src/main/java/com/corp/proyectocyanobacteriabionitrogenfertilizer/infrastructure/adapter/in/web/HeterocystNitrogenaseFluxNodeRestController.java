package com.corp.proyectocyanobacteriabionitrogenfertilizer.infrastructure.adapter.in.web;

import com.corp.proyectocyanobacteriabionitrogenfertilizer.domain.model.HeterocystNitrogenaseFluxNode;
import com.corp.proyectocyanobacteriabionitrogenfertilizer.domain.port.in.ManageHeterocystNitrogenaseFluxNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectocyanobacteriabionitrogenfertilizer")
public class HeterocystNitrogenaseFluxNodeRestController {

    private final ManageHeterocystNitrogenaseFluxNodeUseCase useCase;

    public HeterocystNitrogenaseFluxNodeRestController(ManageHeterocystNitrogenaseFluxNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HeterocystNitrogenaseFluxNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HeterocystNitrogenaseFluxNode created = useCase.createHeterocystNitrogenaseFluxNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectocyanobacteriabionitrogenfertilizer/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeterocystNitrogenaseFluxNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHeterocystNitrogenaseFluxNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
