package com.corp.proyectoconfidentialdatacleanroom.infrastructure.adapter.in.web;

import com.corp.proyectoconfidentialdatacleanroom.domain.model.SecureEnclaveAnalyticsAttestationToken;
import com.corp.proyectoconfidentialdatacleanroom.domain.port.in.ManageSecureEnclaveAnalyticsAttestationTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoconfidentialdatacleanroom")
public class SecureEnclaveAnalyticsAttestationTokenRestController {

    private final ManageSecureEnclaveAnalyticsAttestationTokenUseCase useCase;

    public SecureEnclaveAnalyticsAttestationTokenRestController(ManageSecureEnclaveAnalyticsAttestationTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SecureEnclaveAnalyticsAttestationToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SecureEnclaveAnalyticsAttestationToken created = useCase.createSecureEnclaveAnalyticsAttestationToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoconfidentialdatacleanroom/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecureEnclaveAnalyticsAttestationToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSecureEnclaveAnalyticsAttestationTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
