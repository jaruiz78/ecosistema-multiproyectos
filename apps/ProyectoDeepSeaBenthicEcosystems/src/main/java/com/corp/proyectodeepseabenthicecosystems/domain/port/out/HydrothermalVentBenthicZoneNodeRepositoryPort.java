package com.corp.proyectodeepseabenthicecosystems.domain.port.out;

import com.corp.proyectodeepseabenthicecosystems.domain.model.HydrothermalVentBenthicZoneNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HydrothermalVentBenthicZoneNodeRepositoryPort {
    HydrothermalVentBenthicZoneNode save(HydrothermalVentBenthicZoneNode entity);
    Optional<HydrothermalVentBenthicZoneNode> findById(String id, String tenantId);
}
