package com.corp.proyectogreenhydrogendesal.infrastructure.adapter.in.web;

import com.corp.proyectogreenhydrogendesal.domain.model.DesalinationElectrolyzerUnit;
import com.corp.proyectogreenhydrogendesal.domain.port.in.ManageDesalinationElectrolyzerUnitUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectogreenhydrogendesal")
public class DesalinationElectrolyzerUnitRestController {

    private final ManageDesalinationElectrolyzerUnitUseCase useCase;

    public DesalinationElectrolyzerUnitRestController(ManageDesalinationElectrolyzerUnitUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<DesalinationElectrolyzerUnit> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        DesalinationElectrolyzerUnit created = useCase.createDesalinationElectrolyzerUnit(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectogreenhydrogendesal/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesalinationElectrolyzerUnit> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findDesalinationElectrolyzerUnitById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
