package com.corp.proyectotokenizedcarbonsatellitemrv.domain.port.in;

import com.corp.proyectotokenizedcarbonsatellitemrv.domain.model.VerifiedCarbonSequestrationCreditToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageVerifiedCarbonSequestrationCreditTokenUseCase {
    VerifiedCarbonSequestrationCreditToken createVerifiedCarbonSequestrationCreditToken(String tenantId, String title, double value);
    Optional<VerifiedCarbonSequestrationCreditToken> findVerifiedCarbonSequestrationCreditTokenById(String id, String tenantId);
    VerifiedCarbonSequestrationCreditToken processOptimization(String id, String tenantId);
}
