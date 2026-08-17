package com.corp.proyectohidrogeno.domain.port.out;

import com.corp.proyectohidrogeno.domain.model.ElectrolyzerUnit;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ElectrolyzerUnitRepositoryPort {
    ElectrolyzerUnit save(ElectrolyzerUnit entity);
    Optional<ElectrolyzerUnit> findById(String id, String tenantId);
}
