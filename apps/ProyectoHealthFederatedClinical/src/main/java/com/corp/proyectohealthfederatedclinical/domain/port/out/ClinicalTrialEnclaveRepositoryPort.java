package com.corp.proyectohealthfederatedclinical.domain.port.out;

import com.corp.proyectohealthfederatedclinical.domain.model.ClinicalTrialEnclave;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ClinicalTrialEnclaveRepositoryPort {
    ClinicalTrialEnclave save(ClinicalTrialEnclave entity);
    Optional<ClinicalTrialEnclave> findById(String id, String tenantId);
}
