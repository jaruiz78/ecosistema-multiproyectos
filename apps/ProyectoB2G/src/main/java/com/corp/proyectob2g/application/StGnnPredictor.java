package com.corp.proyectob2g.application;
import com.corp.proyectob2g.domain.automata.CellularState;
import com.corp.proyectob2g.domain.privacy.ZeroPiiEntity;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Arquitectura y especificación formal para StGnnPredictor.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public class StGnnPredictor {
    public List<CellularState> predictSaturation(List<CellularState> grid, List<ZeroPiiEntity> entities) {
        long validEntities = entities.stream().filter(ZeroPiiEntity::isValid).count();
        return grid.stream()
            .map(c -> c.propagate((int) validEntities))
            .collect(Collectors.toList());
    }
}
