package com.corp.proyectoorbitalrefuelingcryostation.infrastructure.adapter.in.web;

import com.corp.proyectoorbitalrefuelingcryostation.domain.model.CryogenicTransferMassBoiloffToken;
import com.corp.proyectoorbitalrefuelingcryostation.domain.port.in.ManageCryogenicTransferMassBoiloffTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoorbitalrefuelingcryostation")
public class CryogenicTransferMassBoiloffTokenRestController {

    private final ManageCryogenicTransferMassBoiloffTokenUseCase useCase;

    public CryogenicTransferMassBoiloffTokenRestController(ManageCryogenicTransferMassBoiloffTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CryogenicTransferMassBoiloffToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CryogenicTransferMassBoiloffToken created = useCase.createCryogenicTransferMassBoiloffToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoorbitalrefuelingcryostation/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CryogenicTransferMassBoiloffToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCryogenicTransferMassBoiloffTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
