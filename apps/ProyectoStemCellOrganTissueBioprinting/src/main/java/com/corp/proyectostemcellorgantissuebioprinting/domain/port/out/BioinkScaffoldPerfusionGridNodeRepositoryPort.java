package com.corp.proyectostemcellorgantissuebioprinting.domain.port.out;

import com.corp.proyectostemcellorgantissuebioprinting.domain.model.BioinkScaffoldPerfusionGridNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface BioinkScaffoldPerfusionGridNodeRepositoryPort {
    BioinkScaffoldPerfusionGridNode save(BioinkScaffoldPerfusionGridNode entity);
    Optional<BioinkScaffoldPerfusionGridNode> findById(String id, String tenantId);
}
