package com.corp.proyectozktaxcomplianceauditor.domain.port.out;

import com.corp.proyectozktaxcomplianceauditor.domain.model.ZkTaxComplianceCertificateToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ZkTaxComplianceCertificateTokenRepositoryPort {
    ZkTaxComplianceCertificateToken save(ZkTaxComplianceCertificateToken entity);
    Optional<ZkTaxComplianceCertificateToken> findById(String id, String tenantId);
}
