package com.corp.proyectobioagritrace.domain.port.in;

import com.corp.proyectobioagritrace.domain.model.BioAgriGenomicSample;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageBioAgriGenomicSampleUseCase {
    BioAgriGenomicSample createBioAgriGenomicSample(String tenantId, String title, double value);
    Optional<BioAgriGenomicSample> findBioAgriGenomicSampleById(String id, String tenantId);
    BioAgriGenomicSample processOptimization(String id, String tenantId);
}
