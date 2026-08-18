package com.corp.proyectogreenhydrogendesal.domain.port.in;

import com.corp.proyectogreenhydrogendesal.domain.model.DesalinationElectrolyzerUnit;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDesalinationElectrolyzerUnitUseCase {
    DesalinationElectrolyzerUnit createDesalinationElectrolyzerUnit(String tenantId, String title, double value);
    Optional<DesalinationElectrolyzerUnit> findDesalinationElectrolyzerUnitById(String id, String tenantId);
    DesalinationElectrolyzerUnit processOptimization(String id, String tenantId);
}
