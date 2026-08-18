package com.corp.proyectozeroknowledgecreditrating.domain.port.out;

import com.corp.proyectozeroknowledgecreditrating.domain.model.ZkCreditEligibilityProofToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ZkCreditEligibilityProofTokenRepositoryPort {
    ZkCreditEligibilityProofToken save(ZkCreditEligibilityProofToken entity);
    Optional<ZkCreditEligibilityProofToken> findById(String id, String tenantId);
}
