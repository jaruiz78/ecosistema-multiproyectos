package com.corp.proyectoquantummetrologycalibration.infrastructure.adapter.in.web;

import com.corp.proyectoquantummetrologycalibration.domain.model.QuantumHallPlateauResistanceToken;
import com.corp.proyectoquantummetrologycalibration.domain.port.in.ManageQuantumHallPlateauResistanceTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantummetrologycalibration")
public class QuantumHallPlateauResistanceTokenRestController {

    private final ManageQuantumHallPlateauResistanceTokenUseCase useCase;

    public QuantumHallPlateauResistanceTokenRestController(ManageQuantumHallPlateauResistanceTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<QuantumHallPlateauResistanceToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        QuantumHallPlateauResistanceToken created = useCase.createQuantumHallPlateauResistanceToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantummetrologycalibration/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuantumHallPlateauResistanceToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findQuantumHallPlateauResistanceTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
