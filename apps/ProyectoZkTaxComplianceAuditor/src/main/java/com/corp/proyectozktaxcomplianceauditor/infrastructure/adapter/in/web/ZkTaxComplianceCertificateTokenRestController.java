package com.corp.proyectozktaxcomplianceauditor.infrastructure.adapter.in.web;

import com.corp.proyectozktaxcomplianceauditor.domain.model.ZkTaxComplianceCertificateToken;
import com.corp.proyectozktaxcomplianceauditor.domain.port.in.ManageZkTaxComplianceCertificateTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tenants/{tenantId}/proyectozktaxcomplianceauditor")
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ZkTaxComplianceCertificateTokenRestController {

    private final ManageZkTaxComplianceCertificateTokenUseCase useCase;

    public ZkTaxComplianceCertificateTokenRestController(ManageZkTaxComplianceCertificateTokenUseCase useCase) {
        this.useCase = useCase;
    }

    /**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
