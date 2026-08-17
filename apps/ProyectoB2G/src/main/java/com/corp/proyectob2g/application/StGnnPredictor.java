package com.corp.proyectob2g.application;

import com.corp.proyectob2g.domain.automata.CellularState;
import com.corp.proyectob2g.domain.privacy.ZeroPiiEntity;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class StGnnPredictor {
    public List<CellularState> predictSaturation(List<CellularState> grid, List<ZeroPiiEntity> entities) {
        int activeEntities = entities.size();
        return grid.stream()
            .map(c -> new CellularState(c.cellId(), c.saturationLevel() + activeEntities))
            .collect(Collectors.toList());
    }
}
