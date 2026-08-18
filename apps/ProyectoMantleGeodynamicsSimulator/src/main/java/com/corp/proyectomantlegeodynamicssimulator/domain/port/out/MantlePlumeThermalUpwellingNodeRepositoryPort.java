package com.corp.proyectomantlegeodynamicssimulator.domain.port.out;

import com.corp.proyectomantlegeodynamicssimulator.domain.model.MantlePlumeThermalUpwellingNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MantlePlumeThermalUpwellingNodeRepositoryPort {
    MantlePlumeThermalUpwellingNode save(MantlePlumeThermalUpwellingNode entity);
    Optional<MantlePlumeThermalUpwellingNode> findById(String id, String tenantId);
}
