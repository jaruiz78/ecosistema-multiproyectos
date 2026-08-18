package com.corp.proyectocyanobacteriabionitrogenfertilizer.domain.port.in;

import com.corp.proyectocyanobacteriabionitrogenfertilizer.domain.model.HeterocystNitrogenaseFluxNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHeterocystNitrogenaseFluxNodeUseCase {
    HeterocystNitrogenaseFluxNode createHeterocystNitrogenaseFluxNode(String tenantId, String title, double value);
    Optional<HeterocystNitrogenaseFluxNode> findHeterocystNitrogenaseFluxNodeById(String id, String tenantId);
    HeterocystNitrogenaseFluxNode processOptimization(String id, String tenantId);
}
