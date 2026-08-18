package com.corp.proyectotourismcarryingcapacitytwin.domain.port.out;

import com.corp.proyectotourismcarryingcapacitytwin.domain.model.EcologicalCarryingCapacityAlertNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface EcologicalCarryingCapacityAlertNodeRepositoryPort {
    EcologicalCarryingCapacityAlertNode save(EcologicalCarryingCapacityAlertNode entity);
    Optional<EcologicalCarryingCapacityAlertNode> findById(String id, String tenantId);
}
