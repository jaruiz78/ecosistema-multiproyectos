package com.corp.proyectozkplonkproofofsolvency.domain.port.out;

import com.corp.proyectozkplonkproofofsolvency.domain.model.PlonkProofOfSolvencyAuditToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PlonkProofOfSolvencyAuditTokenRepositoryPort {
    PlonkProofOfSolvencyAuditToken save(PlonkProofOfSolvencyAuditToken entity);
    Optional<PlonkProofOfSolvencyAuditToken> findById(String id, String tenantId);
}
