package com.corp.proyectoprecisionsoilregen.domain.port.in;

import com.corp.proyectoprecisionsoilregen.domain.model.SoilCarbonMeasurement;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSoilCarbonMeasurementUseCase {
    SoilCarbonMeasurement createSoilCarbonMeasurement(String tenantId, String title, double value);
    Optional<SoilCarbonMeasurement> findSoilCarbonMeasurementById(String id, String tenantId);
    SoilCarbonMeasurement processOptimization(String id, String tenantId);
}
