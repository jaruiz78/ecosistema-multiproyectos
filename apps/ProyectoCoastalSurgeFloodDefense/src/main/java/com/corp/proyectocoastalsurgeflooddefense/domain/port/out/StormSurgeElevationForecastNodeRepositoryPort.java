package com.corp.proyectocoastalsurgeflooddefense.domain.port.out;

import com.corp.proyectocoastalsurgeflooddefense.domain.model.StormSurgeElevationForecastNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface StormSurgeElevationForecastNodeRepositoryPort {
    StormSurgeElevationForecastNode save(StormSurgeElevationForecastNode entity);
    Optional<StormSurgeElevationForecastNode> findById(String id, String tenantId);
}
