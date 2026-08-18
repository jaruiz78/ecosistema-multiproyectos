package com.corp.proyectohydrogenfuelcelllongrangedrone.domain.port.out;

import com.corp.proyectohydrogenfuelcelllongrangedrone.domain.model.PemFuelCellStackEfficiencyNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PemFuelCellStackEfficiencyNodeRepositoryPort {
    PemFuelCellStackEfficiencyNode save(PemFuelCellStackEfficiencyNode entity);
    Optional<PemFuelCellStackEfficiencyNode> findById(String id, String tenantId);
}
