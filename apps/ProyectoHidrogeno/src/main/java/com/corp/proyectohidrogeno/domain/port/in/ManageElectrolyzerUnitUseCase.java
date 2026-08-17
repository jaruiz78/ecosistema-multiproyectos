package com.corp.proyectohidrogeno.domain.port.in;

import com.corp.proyectohidrogeno.domain.model.ElectrolyzerUnit;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageElectrolyzerUnitUseCase {
    ElectrolyzerUnit createElectrolyzerUnit(String tenantId, String title, double value);
    Optional<ElectrolyzerUnit> findElectrolyzerUnitById(String id, String tenantId);
    ElectrolyzerUnit processOptimization(String id, String tenantId);
}
