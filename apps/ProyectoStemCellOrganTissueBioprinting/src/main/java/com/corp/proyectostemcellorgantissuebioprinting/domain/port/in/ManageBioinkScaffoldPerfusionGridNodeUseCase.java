package com.corp.proyectostemcellorgantissuebioprinting.domain.port.in;

import com.corp.proyectostemcellorgantissuebioprinting.domain.model.BioinkScaffoldPerfusionGridNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageBioinkScaffoldPerfusionGridNodeUseCase {
    BioinkScaffoldPerfusionGridNode createBioinkScaffoldPerfusionGridNode(String tenantId, String title, double value);
    Optional<BioinkScaffoldPerfusionGridNode> findBioinkScaffoldPerfusionGridNodeById(String id, String tenantId);
    BioinkScaffoldPerfusionGridNode processOptimization(String id, String tenantId);
}
