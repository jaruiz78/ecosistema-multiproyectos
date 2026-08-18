package com.corp.proyectobiometricprivacysovereignauth.infrastructure.adapter.in.web;

import com.corp.proyectobiometricprivacysovereignauth.domain.model.EncryptedBiometricVectorDistanceNode;
import com.corp.proyectobiometricprivacysovereignauth.domain.port.in.ManageEncryptedBiometricVectorDistanceNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectobiometricprivacysovereignauth")
public class EncryptedBiometricVectorDistanceNodeRestController {

    private final ManageEncryptedBiometricVectorDistanceNodeUseCase useCase;

    public EncryptedBiometricVectorDistanceNodeRestController(ManageEncryptedBiometricVectorDistanceNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<EncryptedBiometricVectorDistanceNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        EncryptedBiometricVectorDistanceNode created = useCase.createEncryptedBiometricVectorDistanceNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectobiometricprivacysovereignauth/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncryptedBiometricVectorDistanceNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findEncryptedBiometricVectorDistanceNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
