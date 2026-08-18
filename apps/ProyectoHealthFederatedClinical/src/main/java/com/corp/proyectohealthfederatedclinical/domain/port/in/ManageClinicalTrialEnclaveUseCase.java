package com.corp.proyectohealthfederatedclinical.domain.port.in;

import com.corp.proyectohealthfederatedclinical.domain.model.ClinicalTrialEnclave;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageClinicalTrialEnclaveUseCase {
    ClinicalTrialEnclave createClinicalTrialEnclave(String tenantId, String title, double value);
    Optional<ClinicalTrialEnclave> findClinicalTrialEnclaveById(String id, String tenantId);
    ClinicalTrialEnclave processOptimization(String id, String tenantId);
}
