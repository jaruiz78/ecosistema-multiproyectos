package com.corp.proyectoeidas2digitalidentitywallet.infrastructure.adapter.in.web;

import com.corp.proyectoeidas2digitalidentitywallet.domain.model.VerifiableCredentialStatusListToken;
import com.corp.proyectoeidas2digitalidentitywallet.domain.port.in.ManageVerifiableCredentialStatusListTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoeidas2digitalidentitywallet")
public class VerifiableCredentialStatusListTokenRestController {

    private final ManageVerifiableCredentialStatusListTokenUseCase useCase;

    public VerifiableCredentialStatusListTokenRestController(ManageVerifiableCredentialStatusListTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<VerifiableCredentialStatusListToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        VerifiableCredentialStatusListToken created = useCase.createVerifiableCredentialStatusListToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoeidas2digitalidentitywallet/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VerifiableCredentialStatusListToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findVerifiableCredentialStatusListTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
