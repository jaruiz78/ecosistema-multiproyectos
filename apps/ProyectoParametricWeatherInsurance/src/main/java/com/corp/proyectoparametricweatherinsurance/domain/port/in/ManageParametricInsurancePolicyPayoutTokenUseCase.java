package com.corp.proyectoparametricweatherinsurance.domain.port.in;

import com.corp.proyectoparametricweatherinsurance.domain.model.ParametricInsurancePolicyPayoutToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageParametricInsurancePolicyPayoutTokenUseCase {
    ParametricInsurancePolicyPayoutToken createParametricInsurancePolicyPayoutToken(String tenantId, String title, double value);
    Optional<ParametricInsurancePolicyPayoutToken> findParametricInsurancePolicyPayoutTokenById(String id, String tenantId);
    ParametricInsurancePolicyPayoutToken processOptimization(String id, String tenantId);
}
