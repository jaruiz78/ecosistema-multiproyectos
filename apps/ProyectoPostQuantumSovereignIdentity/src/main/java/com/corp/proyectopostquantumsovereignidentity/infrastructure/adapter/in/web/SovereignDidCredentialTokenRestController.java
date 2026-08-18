package com.corp.proyectopostquantumsovereignidentity.infrastructure.adapter.in.web;

import com.corp.proyectopostquantumsovereignidentity.domain.model.SovereignDidCredentialToken;
import com.corp.proyectopostquantumsovereignidentity.domain.port.in.ManageSovereignDidCredentialTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectopostquantumsovereignidentity")
public class SovereignDidCredentialTokenRestController {

    private final ManageSovereignDidCredentialTokenUseCase useCase;

    public SovereignDidCredentialTokenRestController(ManageSovereignDidCredentialTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SovereignDidCredentialToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SovereignDidCredentialToken created = useCase.createSovereignDidCredentialToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectopostquantumsovereignidentity/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SovereignDidCredentialToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSovereignDidCredentialTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
