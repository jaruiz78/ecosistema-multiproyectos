package com.corp.proyectoenergia.application;

import com.corp.proyectoenergia.domain.grid.PowerNode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion">FACULTAD_V: Gemelo Digital PEPS, EnKF & Física</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class LinearOpfDispatcher {
    public List<PowerNode> dispatchPower(List<PowerNode> grid, double additionalLoad) {
        double loadPerNode = additionalLoad / grid.size();
        return grid.stream()
            .map(n -> new PowerNode(n.nodeId(), n.generationCapacity(), n.currentLoad() + loadPerNode))
            .collect(Collectors.toList());
    }
}
