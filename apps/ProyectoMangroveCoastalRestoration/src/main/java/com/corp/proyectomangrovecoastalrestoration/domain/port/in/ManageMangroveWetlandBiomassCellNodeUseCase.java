package com.corp.proyectomangrovecoastalrestoration.domain.port.in;

import com.corp.proyectomangrovecoastalrestoration.domain.model.MangroveWetlandBiomassCellNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMangroveWetlandBiomassCellNodeUseCase {
    MangroveWetlandBiomassCellNode createMangroveWetlandBiomassCellNode(String tenantId, String title, double value);
    Optional<MangroveWetlandBiomassCellNode> findMangroveWetlandBiomassCellNodeById(String id, String tenantId);
    MangroveWetlandBiomassCellNode processOptimization(String id, String tenantId);
}
