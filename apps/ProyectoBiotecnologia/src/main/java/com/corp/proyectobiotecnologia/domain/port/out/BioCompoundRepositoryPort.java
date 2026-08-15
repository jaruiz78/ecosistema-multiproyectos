package com.corp.proyectobiotecnologia.domain.port.out;

import com.corp.proyectobiotecnologia.domain.model.BioCompound;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface BioCompoundRepositoryPort {
    BioCompound save(BioCompound entity);
    Optional<BioCompound> findById(String id, String tenantId);
}
