package com.corp.proyectologistica.application;
import com.corp.proyectologistica.domain.vrp.RouteRecord;
import com.corp.proyectologistica.domain.spatial.H3GeoIndex;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Arquitectura y especificación formal para VrpOptimizerService.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public class VrpOptimizerService {
    public List<RouteRecord> optimizeRoutes(List<RouteRecord> activeRoutes, H3GeoIndex surgeContext) {
        return activeRoutes.stream()
            .map(r -> new RouteRecord(r.routeId(), surgeContext.h3CellId(), r.priorityScore() * surgeContext.demandSurgeFactor(), r.isEscrowSettled()))
            .sorted((a, b) -> Double.compare(b.priorityScore(), a.priorityScore()))
            .collect(Collectors.toList());
    }
}
