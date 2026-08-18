package com.corp.proyectodeepseabenthicecosystems.domain.port.in;

import com.corp.proyectodeepseabenthicecosystems.domain.model.HydrothermalVentBenthicZoneNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHydrothermalVentBenthicZoneNodeUseCase {
    HydrothermalVentBenthicZoneNode createHydrothermalVentBenthicZoneNode(String tenantId, String title, double value);
    Optional<HydrothermalVentBenthicZoneNode> findHydrothermalVentBenthicZoneNodeById(String id, String tenantId);
    HydrothermalVentBenthicZoneNode processOptimization(String id, String tenantId);
}
