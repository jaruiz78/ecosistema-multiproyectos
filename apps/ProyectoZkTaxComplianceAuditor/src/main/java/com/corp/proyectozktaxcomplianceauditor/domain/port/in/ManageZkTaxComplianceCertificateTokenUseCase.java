package com.corp.proyectozktaxcomplianceauditor.domain.port.in;

import com.corp.proyectozktaxcomplianceauditor.domain.model.ZkTaxComplianceCertificateToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageZkTaxComplianceCertificateTokenUseCase {
    ZkTaxComplianceCertificateToken createZkTaxComplianceCertificateToken(String tenantId, String title, double value);
    Optional<ZkTaxComplianceCertificateToken> findZkTaxComplianceCertificateTokenById(String id, String tenantId);
    ZkTaxComplianceCertificateToken processOptimization(String id, String tenantId);
}
