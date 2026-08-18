package com.corp.proyectogreenhydrogendesal.domain.port.out;

import com.corp.proyectogreenhydrogendesal.domain.model.DesalinationElectrolyzerUnit;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DesalinationElectrolyzerUnitRepositoryPort {
    DesalinationElectrolyzerUnit save(DesalinationElectrolyzerUnit entity);
    Optional<DesalinationElectrolyzerUnit> findById(String id, String tenantId);
}
