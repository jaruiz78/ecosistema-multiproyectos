package com.corp.proyectoartificialchloroplastcarbonsink.infrastructure.adapter.in.web;

import com.corp.proyectoartificialchloroplastcarbonsink.domain.model.SyntheticThylakoidEfficiencyToken;
import com.corp.proyectoartificialchloroplastcarbonsink.domain.port.in.ManageSyntheticThylakoidEfficiencyTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoartificialchloroplastcarbonsink")
public class SyntheticThylakoidEfficiencyTokenRestController {

    private final ManageSyntheticThylakoidEfficiencyTokenUseCase useCase;

    public SyntheticThylakoidEfficiencyTokenRestController(ManageSyntheticThylakoidEfficiencyTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SyntheticThylakoidEfficiencyToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SyntheticThylakoidEfficiencyToken created = useCase.createSyntheticThylakoidEfficiencyToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoartificialchloroplastcarbonsink/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SyntheticThylakoidEfficiencyToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSyntheticThylakoidEfficiencyTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
