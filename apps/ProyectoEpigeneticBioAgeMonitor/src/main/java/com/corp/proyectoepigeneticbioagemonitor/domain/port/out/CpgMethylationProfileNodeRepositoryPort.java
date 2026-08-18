package com.corp.proyectoepigeneticbioagemonitor.domain.port.out;

import com.corp.proyectoepigeneticbioagemonitor.domain.model.CpgMethylationProfileNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CpgMethylationProfileNodeRepositoryPort {
    CpgMethylationProfileNode save(CpgMethylationProfileNode entity);
    Optional<CpgMethylationProfileNode> findById(String id, String tenantId);
}
