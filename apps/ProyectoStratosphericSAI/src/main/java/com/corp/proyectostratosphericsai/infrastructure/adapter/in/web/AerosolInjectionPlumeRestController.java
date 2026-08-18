package com.corp.proyectostratosphericsai.infrastructure.adapter.in.web;

import com.corp.proyectostratosphericsai.domain.model.AerosolInjectionPlume;
import com.corp.proyectostratosphericsai.domain.port.in.ManageAerosolInjectionPlumeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectostratosphericsai")
public class AerosolInjectionPlumeRestController {

    private final ManageAerosolInjectionPlumeUseCase useCase;

    public AerosolInjectionPlumeRestController(ManageAerosolInjectionPlumeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<AerosolInjectionPlume> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        AerosolInjectionPlume created = useCase.createAerosolInjectionPlume(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectostratosphericsai/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AerosolInjectionPlume> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findAerosolInjectionPlumeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
