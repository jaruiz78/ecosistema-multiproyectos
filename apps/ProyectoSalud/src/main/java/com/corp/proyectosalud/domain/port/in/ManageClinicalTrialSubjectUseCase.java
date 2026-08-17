package com.corp.proyectosalud.domain.port.in;

import com.corp.proyectosalud.domain.model.ClinicalTrialSubject;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageClinicalTrialSubjectUseCase {
    ClinicalTrialSubject createClinicalTrialSubject(String tenantId, String title, double value);
    Optional<ClinicalTrialSubject> findClinicalTrialSubjectById(String id, String tenantId);
    ClinicalTrialSubject processOptimization(String id, String tenantId);
}
