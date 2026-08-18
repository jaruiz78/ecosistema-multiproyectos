package com.corp.proyectodiscretevariableqkdmesh.domain.port.out;

import com.corp.proyectodiscretevariableqkdmesh.domain.model.DvQkdDecoyStateKeyStreamToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DvQkdDecoyStateKeyStreamTokenRepositoryPort {
    DvQkdDecoyStateKeyStreamToken save(DvQkdDecoyStateKeyStreamToken entity);
    Optional<DvQkdDecoyStateKeyStreamToken> findById(String id, String tenantId);
}
