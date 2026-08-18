package com.corp.proyectocyanobacteriabionitrogenfertilizer.domain.port.out;

import com.corp.proyectocyanobacteriabionitrogenfertilizer.domain.model.HeterocystNitrogenaseFluxNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HeterocystNitrogenaseFluxNodeRepositoryPort {
    HeterocystNitrogenaseFluxNode save(HeterocystNitrogenaseFluxNode entity);
    Optional<HeterocystNitrogenaseFluxNode> findById(String id, String tenantId);
}
