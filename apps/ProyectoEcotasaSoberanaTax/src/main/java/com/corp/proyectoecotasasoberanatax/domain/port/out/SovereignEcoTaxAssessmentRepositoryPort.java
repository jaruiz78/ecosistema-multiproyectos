package com.corp.proyectoecotasasoberanatax.domain.port.out;

import com.corp.proyectoecotasasoberanatax.domain.model.SovereignEcoTaxAssessment;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SovereignEcoTaxAssessmentRepositoryPort {
    SovereignEcoTaxAssessment save(SovereignEcoTaxAssessment entity);
    Optional<SovereignEcoTaxAssessment> findById(String id, String tenantId);
}
