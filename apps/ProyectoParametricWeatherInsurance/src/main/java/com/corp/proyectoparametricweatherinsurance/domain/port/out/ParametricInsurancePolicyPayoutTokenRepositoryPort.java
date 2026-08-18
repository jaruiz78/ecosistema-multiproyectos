package com.corp.proyectoparametricweatherinsurance.domain.port.out;

import com.corp.proyectoparametricweatherinsurance.domain.model.ParametricInsurancePolicyPayoutToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ParametricInsurancePolicyPayoutTokenRepositoryPort {
    ParametricInsurancePolicyPayoutToken save(ParametricInsurancePolicyPayoutToken entity);
    Optional<ParametricInsurancePolicyPayoutToken> findById(String id, String tenantId);
}
