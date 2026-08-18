package com.corp.proyectosuborbitalspacetourismtwin.domain.port.out;

import com.corp.proyectosuborbitalspacetourismtwin.domain.model.SpaceTourismPassengerGForceNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SpaceTourismPassengerGForceNodeRepositoryPort {
    SpaceTourismPassengerGForceNode save(SpaceTourismPassengerGForceNode entity);
    Optional<SpaceTourismPassengerGForceNode> findById(String id, String tenantId);
}
