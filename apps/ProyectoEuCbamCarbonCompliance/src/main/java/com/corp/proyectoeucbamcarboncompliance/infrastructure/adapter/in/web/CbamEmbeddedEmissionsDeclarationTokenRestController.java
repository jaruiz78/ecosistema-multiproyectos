package com.corp.proyectoeucbamcarboncompliance.infrastructure.adapter.in.web;

import com.corp.proyectoeucbamcarboncompliance.domain.model.CbamEmbeddedEmissionsDeclarationToken;
import com.corp.proyectoeucbamcarboncompliance.domain.port.in.ManageCbamEmbeddedEmissionsDeclarationTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoeucbamcarboncompliance")
public class CbamEmbeddedEmissionsDeclarationTokenRestController {

    private final ManageCbamEmbeddedEmissionsDeclarationTokenUseCase useCase;

    public CbamEmbeddedEmissionsDeclarationTokenRestController(ManageCbamEmbeddedEmissionsDeclarationTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CbamEmbeddedEmissionsDeclarationToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CbamEmbeddedEmissionsDeclarationToken created = useCase.createCbamEmbeddedEmissionsDeclarationToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoeucbamcarboncompliance/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CbamEmbeddedEmissionsDeclarationToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCbamEmbeddedEmissionsDeclarationTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
