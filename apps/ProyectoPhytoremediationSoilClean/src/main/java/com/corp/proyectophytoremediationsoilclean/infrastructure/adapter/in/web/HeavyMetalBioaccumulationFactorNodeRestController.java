package com.corp.proyectophytoremediationsoilclean.infrastructure.adapter.in.web;

import com.corp.proyectophytoremediationsoilclean.domain.model.HeavyMetalBioaccumulationFactorNode;
import com.corp.proyectophytoremediationsoilclean.domain.port.in.ManageHeavyMetalBioaccumulationFactorNodeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectophytoremediationsoilclean")
public class HeavyMetalBioaccumulationFactorNodeRestController {

    private final ManageHeavyMetalBioaccumulationFactorNodeUseCase useCase;

    public HeavyMetalBioaccumulationFactorNodeRestController(ManageHeavyMetalBioaccumulationFactorNodeUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<HeavyMetalBioaccumulationFactorNode> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        HeavyMetalBioaccumulationFactorNode created = useCase.createHeavyMetalBioaccumulationFactorNode(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectophytoremediationsoilclean/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeavyMetalBioaccumulationFactorNode> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findHeavyMetalBioaccumulationFactorNodeById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
