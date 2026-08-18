package com.corp.proyectomangrovecoastalrestoration.domain.port.out;

import com.corp.proyectomangrovecoastalrestoration.domain.model.MangroveWetlandBiomassCellNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MangroveWetlandBiomassCellNodeRepositoryPort {
    MangroveWetlandBiomassCellNode save(MangroveWetlandBiomassCellNode entity);
    Optional<MangroveWetlandBiomassCellNode> findById(String id, String tenantId);
}
