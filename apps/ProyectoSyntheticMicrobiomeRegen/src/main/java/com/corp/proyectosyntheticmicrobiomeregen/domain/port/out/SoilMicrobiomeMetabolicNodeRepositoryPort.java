package com.corp.proyectosyntheticmicrobiomeregen.domain.port.out;

import com.corp.proyectosyntheticmicrobiomeregen.domain.model.SoilMicrobiomeMetabolicNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SoilMicrobiomeMetabolicNodeRepositoryPort {
    SoilMicrobiomeMetabolicNode save(SoilMicrobiomeMetabolicNode entity);
    Optional<SoilMicrobiomeMetabolicNode> findById(String id, String tenantId);
}
