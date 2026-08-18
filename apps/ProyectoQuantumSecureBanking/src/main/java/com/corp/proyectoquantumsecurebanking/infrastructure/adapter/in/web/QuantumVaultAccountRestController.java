package com.corp.proyectoquantumsecurebanking.infrastructure.adapter.in.web;

import com.corp.proyectoquantumsecurebanking.domain.model.QuantumVaultAccount;
import com.corp.proyectoquantumsecurebanking.domain.port.in.ManageQuantumVaultAccountUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoquantumsecurebanking")
public class QuantumVaultAccountRestController {

    private final ManageQuantumVaultAccountUseCase useCase;

    public QuantumVaultAccountRestController(ManageQuantumVaultAccountUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<QuantumVaultAccount> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        QuantumVaultAccount created = useCase.createQuantumVaultAccount(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoquantumsecurebanking/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuantumVaultAccount> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findQuantumVaultAccountById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
