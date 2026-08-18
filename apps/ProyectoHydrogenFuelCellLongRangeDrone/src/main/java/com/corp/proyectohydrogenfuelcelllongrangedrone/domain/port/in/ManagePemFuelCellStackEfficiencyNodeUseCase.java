package com.corp.proyectohydrogenfuelcelllongrangedrone.domain.port.in;

import com.corp.proyectohydrogenfuelcelllongrangedrone.domain.model.PemFuelCellStackEfficiencyNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePemFuelCellStackEfficiencyNodeUseCase {
    PemFuelCellStackEfficiencyNode createPemFuelCellStackEfficiencyNode(String tenantId, String title, double value);
    Optional<PemFuelCellStackEfficiencyNode> findPemFuelCellStackEfficiencyNodeById(String id, String tenantId);
    PemFuelCellStackEfficiencyNode processOptimization(String id, String tenantId);
}
