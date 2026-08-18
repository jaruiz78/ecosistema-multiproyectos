package com.corp.proyectoecotasasoberanatax.domain.port.in;

import com.corp.proyectoecotasasoberanatax.domain.model.SovereignEcoTaxAssessment;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSovereignEcoTaxAssessmentUseCase {
    SovereignEcoTaxAssessment createSovereignEcoTaxAssessment(String tenantId, String title, double value);
    Optional<SovereignEcoTaxAssessment> findSovereignEcoTaxAssessmentById(String id, String tenantId);
    SovereignEcoTaxAssessment processOptimization(String id, String tenantId);
}
