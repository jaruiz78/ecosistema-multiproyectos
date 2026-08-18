package com.corp.proyectoallostericdrugdiscovery.infrastructure.adapter.in.web;

import com.corp.proyectoallostericdrugdiscovery.domain.model.CrypticBindingPocketVolumeToken;
import com.corp.proyectoallostericdrugdiscovery.domain.port.in.ManageCrypticBindingPocketVolumeTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoallostericdrugdiscovery")
public class CrypticBindingPocketVolumeTokenRestController {

    private final ManageCrypticBindingPocketVolumeTokenUseCase useCase;

    public CrypticBindingPocketVolumeTokenRestController(ManageCrypticBindingPocketVolumeTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<CrypticBindingPocketVolumeToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        CrypticBindingPocketVolumeToken created = useCase.createCrypticBindingPocketVolumeToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoallostericdrugdiscovery/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrypticBindingPocketVolumeToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findCrypticBindingPocketVolumeTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
