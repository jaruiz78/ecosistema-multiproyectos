package com.corp.proyectodecentralizedclinicalbiologistics.domain.port.out;

import com.corp.proyectodecentralizedclinicalbiologistics.domain.model.ClinicalTrialBioSampleToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ClinicalTrialBioSampleTokenRepositoryPort {
    ClinicalTrialBioSampleToken save(ClinicalTrialBioSampleToken entity);
    Optional<ClinicalTrialBioSampleToken> findById(String id, String tenantId);
}
