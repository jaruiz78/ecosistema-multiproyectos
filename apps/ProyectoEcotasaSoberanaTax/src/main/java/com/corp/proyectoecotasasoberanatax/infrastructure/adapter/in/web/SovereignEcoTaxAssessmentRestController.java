package com.corp.proyectoecotasasoberanatax.infrastructure.adapter.in.web;

import com.corp.proyectoecotasasoberanatax.domain.model.SovereignEcoTaxAssessment;
import com.corp.proyectoecotasasoberanatax.domain.port.in.ManageSovereignEcoTaxAssessmentUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectoecotasasoberanatax")
public class SovereignEcoTaxAssessmentRestController {

    private final ManageSovereignEcoTaxAssessmentUseCase useCase;

    public SovereignEcoTaxAssessmentRestController(ManageSovereignEcoTaxAssessmentUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<SovereignEcoTaxAssessment> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        SovereignEcoTaxAssessment created = useCase.createSovereignEcoTaxAssessment(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectoecotasasoberanatax/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SovereignEcoTaxAssessment> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findSovereignEcoTaxAssessmentById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
