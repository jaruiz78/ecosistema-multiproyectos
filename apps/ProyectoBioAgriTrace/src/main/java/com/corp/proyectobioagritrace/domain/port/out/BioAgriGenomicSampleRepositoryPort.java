package com.corp.proyectobioagritrace.domain.port.out;

import com.corp.proyectobioagritrace.domain.model.BioAgriGenomicSample;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface BioAgriGenomicSampleRepositoryPort {
    BioAgriGenomicSample save(BioAgriGenomicSample entity);
    Optional<BioAgriGenomicSample> findById(String id, String tenantId);
}
