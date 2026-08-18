package com.corp.proyectotokenizedcarbonsatellitemrv.domain.port.out;

import com.corp.proyectotokenizedcarbonsatellitemrv.domain.model.VerifiedCarbonSequestrationCreditToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface VerifiedCarbonSequestrationCreditTokenRepositoryPort {
    VerifiedCarbonSequestrationCreditToken save(VerifiedCarbonSequestrationCreditToken entity);
    Optional<VerifiedCarbonSequestrationCreditToken> findById(String id, String tenantId);
}
