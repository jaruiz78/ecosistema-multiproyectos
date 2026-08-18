package com.corp.proyectopresatwinscada.domain.port.out;

import com.corp.proyectopresatwinscada.domain.model.DamTelemetrySensorNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DamTelemetrySensorNodeRepositoryPort {
    DamTelemetrySensorNode save(DamTelemetrySensorNode entity);
    Optional<DamTelemetrySensorNode> findById(String id, String tenantId);
}
