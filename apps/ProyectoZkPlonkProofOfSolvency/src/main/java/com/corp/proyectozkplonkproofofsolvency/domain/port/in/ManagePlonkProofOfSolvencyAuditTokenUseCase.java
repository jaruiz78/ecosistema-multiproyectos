package com.corp.proyectozkplonkproofofsolvency.domain.port.in;

import com.corp.proyectozkplonkproofofsolvency.domain.model.PlonkProofOfSolvencyAuditToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePlonkProofOfSolvencyAuditTokenUseCase {
    PlonkProofOfSolvencyAuditToken createPlonkProofOfSolvencyAuditToken(String tenantId, String title, double value);
    Optional<PlonkProofOfSolvencyAuditToken> findPlonkProofOfSolvencyAuditTokenById(String id, String tenantId);
    PlonkProofOfSolvencyAuditToken processOptimization(String id, String tenantId);
}
