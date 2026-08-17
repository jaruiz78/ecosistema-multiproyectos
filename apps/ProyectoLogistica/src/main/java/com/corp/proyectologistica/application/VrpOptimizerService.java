package com.corp.proyectologistica.application;

import com.corp.proyectologistica.domain.vrp.RouteRecord;
import java.util.List;

import com.corp.proyectologistica.domain.spatial.H3GeoIndex;
import java.util.stream.Collectors;
import java.util.Comparator;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class VrpOptimizerService {
    public List<RouteRecord> optimizeRoutes(List<RouteRecord> routes, H3GeoIndex index) {
        return routes.stream()
            .map(r -> new RouteRecord(r.routeId(), index.h3CellId(), r.priorityScore() * index.demandSurgeFactor(), r.isEscrowSettled()))
            .sorted(Comparator.comparing(RouteRecord::priorityScore).reversed())
            .collect(Collectors.toList());
    }
}
