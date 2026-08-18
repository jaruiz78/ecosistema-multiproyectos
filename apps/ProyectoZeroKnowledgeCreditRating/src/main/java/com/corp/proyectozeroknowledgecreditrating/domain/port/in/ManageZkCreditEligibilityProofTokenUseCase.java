package com.corp.proyectozeroknowledgecreditrating.domain.port.in;

import com.corp.proyectozeroknowledgecreditrating.domain.model.ZkCreditEligibilityProofToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageZkCreditEligibilityProofTokenUseCase {
    ZkCreditEligibilityProofToken createZkCreditEligibilityProofToken(String tenantId, String title, double value);
    Optional<ZkCreditEligibilityProofToken> findZkCreditEligibilityProofTokenById(String id, String tenantId);
    ZkCreditEligibilityProofToken processOptimization(String id, String tenantId);
}
