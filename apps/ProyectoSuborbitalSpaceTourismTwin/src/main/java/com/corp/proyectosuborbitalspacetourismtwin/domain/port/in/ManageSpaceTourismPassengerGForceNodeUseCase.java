package com.corp.proyectosuborbitalspacetourismtwin.domain.port.in;

import com.corp.proyectosuborbitalspacetourismtwin.domain.model.SpaceTourismPassengerGForceNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSpaceTourismPassengerGForceNodeUseCase {
    SpaceTourismPassengerGForceNode createSpaceTourismPassengerGForceNode(String tenantId, String title, double value);
    Optional<SpaceTourismPassengerGForceNode> findSpaceTourismPassengerGForceNodeById(String id, String tenantId);
    SpaceTourismPassengerGForceNode processOptimization(String id, String tenantId);
}
