package com.corp.proyectocoastalsurgeflooddefense.domain.port.in;

import com.corp.proyectocoastalsurgeflooddefense.domain.model.StormSurgeElevationForecastNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface ManageStormSurgeElevationForecastNodeUseCase {
    StormSurgeElevationForecastNode createStormSurgeElevationForecastNode(String tenantId, String title, double value);
    Optional<StormSurgeElevationForecastNode> findStormSurgeElevationForecastNodeById(String id, String tenantId);
    StormSurgeElevationForecastNode processOptimization(String id, String tenantId);
}
