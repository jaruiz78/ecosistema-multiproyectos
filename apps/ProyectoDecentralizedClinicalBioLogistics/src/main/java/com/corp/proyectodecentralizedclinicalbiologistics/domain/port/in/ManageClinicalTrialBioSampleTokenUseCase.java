package com.corp.proyectodecentralizedclinicalbiologistics.domain.port.in;

import com.corp.proyectodecentralizedclinicalbiologistics.domain.model.ClinicalTrialBioSampleToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageClinicalTrialBioSampleTokenUseCase {
    ClinicalTrialBioSampleToken createClinicalTrialBioSampleToken(String tenantId, String title, double value);
    Optional<ClinicalTrialBioSampleToken> findClinicalTrialBioSampleTokenById(String id, String tenantId);
    ClinicalTrialBioSampleToken processOptimization(String id, String tenantId);
}
