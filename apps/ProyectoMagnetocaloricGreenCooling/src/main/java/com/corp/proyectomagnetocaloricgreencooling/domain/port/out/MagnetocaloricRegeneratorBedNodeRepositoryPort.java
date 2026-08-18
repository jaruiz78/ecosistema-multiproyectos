package com.corp.proyectomagnetocaloricgreencooling.domain.port.out;

import com.corp.proyectomagnetocaloricgreencooling.domain.model.MagnetocaloricRegeneratorBedNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MagnetocaloricRegeneratorBedNodeRepositoryPort {
    MagnetocaloricRegeneratorBedNode save(MagnetocaloricRegeneratorBedNode entity);
    Optional<MagnetocaloricRegeneratorBedNode> findById(String id, String tenantId);
}
