package com.corp.proyectosalud.domain.port.in;

import com.corp.proyectosalud.domain.model.ClinicalTrialSample;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageClinicalTrialSampleUseCase {
    ClinicalTrialSample createClinicalTrialSample(String tenantId, String title, double value);
    Optional<ClinicalTrialSample> findClinicalTrialSampleById(String id, String tenantId);
    ClinicalTrialSample processOptimization(String id, String tenantId);
}
