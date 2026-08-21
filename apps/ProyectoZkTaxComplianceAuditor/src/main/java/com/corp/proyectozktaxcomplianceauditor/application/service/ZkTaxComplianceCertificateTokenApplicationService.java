package com.corp.proyectozktaxcomplianceauditor.application.service;

import com.corp.proyectozktaxcomplianceauditor.domain.model.ZkTaxComplianceCertificateToken;
import com.corp.proyectozktaxcomplianceauditor.domain.port.in.ManageZkTaxComplianceCertificateTokenUseCase;
import com.corp.proyectozktaxcomplianceauditor.domain.port.out.ZkTaxComplianceCertificateTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ZkTaxComplianceCertificateToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ZkTaxComplianceCertificateTokenApplicationService implements ManageZkTaxComplianceCertificateTokenUseCase {

    private final ZkTaxComplianceCertificateTokenRepositoryPort repositoryPort;

    public ZkTaxComplianceCertificateTokenApplicationService(ZkTaxComplianceCertificateTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ZkTaxComplianceCertificateToken createZkTaxComplianceCertificateToken(String tenantId, String title, double value) {
        ZkTaxComplianceCertificateToken entity = new ZkTaxComplianceCertificateToken(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<ZkTaxComplianceCertificateToken> findZkTaxComplianceCertificateTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ZkTaxComplianceCertificateToken processOptimization(String id, String tenantId) {
        ZkTaxComplianceCertificateToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ZkTaxComplianceCertificateToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
