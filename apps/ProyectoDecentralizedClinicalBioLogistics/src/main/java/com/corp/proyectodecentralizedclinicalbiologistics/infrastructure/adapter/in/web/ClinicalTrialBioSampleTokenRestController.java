package com.corp.proyectodecentralizedclinicalbiologistics.infrastructure.adapter.in.web;

import com.corp.proyectodecentralizedclinicalbiologistics.domain.model.ClinicalTrialBioSampleToken;
import com.corp.proyectodecentralizedclinicalbiologistics.domain.port.in.ManageClinicalTrialBioSampleTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectodecentralizedclinicalbiologistics")
public class ClinicalTrialBioSampleTokenRestController {

    private final ManageClinicalTrialBioSampleTokenUseCase useCase;

    public ClinicalTrialBioSampleTokenRestController(ManageClinicalTrialBioSampleTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ClinicalTrialBioSampleToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ClinicalTrialBioSampleToken created = useCase.createClinicalTrialBioSampleToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectodecentralizedclinicalbiologistics/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalTrialBioSampleToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findClinicalTrialBioSampleTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
