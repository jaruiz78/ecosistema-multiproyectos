package com.corp.proyectoprecisionsoilregen.domain.port.out;

import com.corp.proyectoprecisionsoilregen.domain.model.SoilCarbonMeasurement;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SoilCarbonMeasurementRepositoryPort {
    SoilCarbonMeasurement save(SoilCarbonMeasurement entity);
    Optional<SoilCarbonMeasurement> findById(String id, String tenantId);
}
