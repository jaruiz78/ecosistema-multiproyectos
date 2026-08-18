package com.corp.proyectotourismcarryingcapacitytwin.domain.port.in;

import com.corp.proyectotourismcarryingcapacitytwin.domain.model.EcologicalCarryingCapacityAlertNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageEcologicalCarryingCapacityAlertNodeUseCase {
    EcologicalCarryingCapacityAlertNode createEcologicalCarryingCapacityAlertNode(String tenantId, String title, double value);
    Optional<EcologicalCarryingCapacityAlertNode> findEcologicalCarryingCapacityAlertNodeById(String id, String tenantId);
    EcologicalCarryingCapacityAlertNode processOptimization(String id, String tenantId);
}
