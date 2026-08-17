package com.corp.proyectohidrogeno.infrastructure.adapter.in.web;

import com.corp.proyectohidrogeno.domain.model.ElectrolyzerUnit;
import com.corp.proyectohidrogeno.domain.port.in.ManageElectrolyzerUnitUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectohidrogeno")
public class ElectrolyzerUnitRestController {

    private final ManageElectrolyzerUnitUseCase useCase;

    public ElectrolyzerUnitRestController(ManageElectrolyzerUnitUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ElectrolyzerUnit> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ElectrolyzerUnit created = useCase.createElectrolyzerUnit(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectohidrogeno/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectrolyzerUnit> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findElectrolyzerUnitById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
