package com.corp.proyectocoastalsurgeflooddefense.domain.port.in;

import com.corp.proyectocoastalsurgeflooddefense.domain.model.StormSurgeElevationForecastNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageStormSurgeElevationForecastNodeUseCase {
    StormSurgeElevationForecastNode createStormSurgeElevationForecastNode(String tenantId, String title, double value);
    Optional<StormSurgeElevationForecastNode> findStormSurgeElevationForecastNodeById(String id, String tenantId);
    StormSurgeElevationForecastNode processOptimization(String id, String tenantId);
}
