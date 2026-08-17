package com.corp.proyectosalud.domain.port.out;

import com.corp.proyectosalud.domain.model.ClinicalTrialSample;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ClinicalTrialSampleRepositoryPort {
    ClinicalTrialSample save(ClinicalTrialSample entity);
    Optional<ClinicalTrialSample> findById(String id, String tenantId);
}
