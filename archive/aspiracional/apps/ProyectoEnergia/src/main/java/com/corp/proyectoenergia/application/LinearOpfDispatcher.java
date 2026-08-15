package com.corp.proyectoenergia.application;
import com.corp.proyectoenergia.domain.grid.PowerNode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Arquitectura y especificación formal para LinearOpfDispatcher.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public class LinearOpfDispatcher {
    public List<PowerNode> dispatchPower(List<PowerNode> grid, double demandSpike) {
        double distributedSpike = demandSpike / grid.size();
        return grid.stream()
            .map(n -> new PowerNode(n.nodeId(), n.generationCapacity(), n.currentLoad() + distributedSpike))
            .collect(Collectors.toList());
    }
}
