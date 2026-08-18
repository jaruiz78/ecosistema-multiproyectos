package com.corp.proyectozktaxcomplianceauditor.infrastructure.adapter.in.web;

import com.corp.proyectozktaxcomplianceauditor.domain.model.ZkTaxComplianceCertificateToken;
import com.corp.proyectozktaxcomplianceauditor.domain.port.in.ManageZkTaxComplianceCertificateTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectozktaxcomplianceauditor")
public class ZkTaxComplianceCertificateTokenRestController {

    private final ManageZkTaxComplianceCertificateTokenUseCase useCase;

    public ZkTaxComplianceCertificateTokenRestController(ManageZkTaxComplianceCertificateTokenUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateRequest(String title, double value) {}

    @PostMapping
    public ResponseEntity<ZkTaxComplianceCertificateToken> create(
            @PathVariable String tenantId,
            @RequestBody CreateRequest request) {
        ZkTaxComplianceCertificateToken created = useCase.createZkTaxComplianceCertificateToken(tenantId, request.title(), request.value());
        return ResponseEntity.created(URI.create("/api/v1/tenants/" + tenantId + "/proyectozktaxcomplianceauditor/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZkTaxComplianceCertificateToken> getById(
            @PathVariable String tenantId,
            @PathVariable String id) {
        return useCase.findZkTaxComplianceCertificateTokenById(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
