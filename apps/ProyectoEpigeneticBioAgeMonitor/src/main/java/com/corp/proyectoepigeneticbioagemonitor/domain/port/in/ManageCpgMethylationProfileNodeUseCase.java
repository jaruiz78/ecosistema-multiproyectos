package com.corp.proyectoepigeneticbioagemonitor.domain.port.in;

import com.corp.proyectoepigeneticbioagemonitor.domain.model.CpgMethylationProfileNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCpgMethylationProfileNodeUseCase {
    CpgMethylationProfileNode createCpgMethylationProfileNode(String tenantId, String title, double value);
    Optional<CpgMethylationProfileNode> findCpgMethylationProfileNodeById(String id, String tenantId);
    CpgMethylationProfileNode processOptimization(String id, String tenantId);
}
