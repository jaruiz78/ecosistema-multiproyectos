package com.corp.proyectopresatwinscada.domain.port.in;

import com.corp.proyectopresatwinscada.domain.model.DamTelemetrySensorNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDamTelemetrySensorNodeUseCase {
    DamTelemetrySensorNode createDamTelemetrySensorNode(String tenantId, String title, double value);
    Optional<DamTelemetrySensorNode> findDamTelemetrySensorNodeById(String id, String tenantId);
    DamTelemetrySensorNode processOptimization(String id, String tenantId);
}
