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
