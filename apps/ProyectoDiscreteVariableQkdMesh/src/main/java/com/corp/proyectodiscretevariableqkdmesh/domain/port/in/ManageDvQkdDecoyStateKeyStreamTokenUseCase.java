package com.corp.proyectodiscretevariableqkdmesh.domain.port.in;

import com.corp.proyectodiscretevariableqkdmesh.domain.model.DvQkdDecoyStateKeyStreamToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDvQkdDecoyStateKeyStreamTokenUseCase {
    DvQkdDecoyStateKeyStreamToken createDvQkdDecoyStateKeyStreamToken(String tenantId, String title, double value);
    Optional<DvQkdDecoyStateKeyStreamToken> findDvQkdDecoyStateKeyStreamTokenById(String id, String tenantId);
    DvQkdDecoyStateKeyStreamToken processOptimization(String id, String tenantId);
}
