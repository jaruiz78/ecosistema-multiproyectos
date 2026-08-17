package com.corp.proyectosalud.domain.port.out;

import com.corp.proyectosalud.domain.model.ClinicalTrialSubject;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ClinicalTrialSubjectRepositoryPort {
    ClinicalTrialSubject save(ClinicalTrialSubject entity);
    Optional<ClinicalTrialSubject> findById(String id, String tenantId);
}
