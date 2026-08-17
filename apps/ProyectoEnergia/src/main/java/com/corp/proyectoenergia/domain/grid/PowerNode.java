package com.corp.proyectoenergia.domain.grid;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion">FACULTAD_V: Gemelo Digital PEPS, EnKF & Física</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record PowerNode(String nodeId, double generationCapacity, double currentLoad) {
    public PowerNode {
        java.util.Objects.requireNonNull(nodeId, "Invariante de Hoare: 'nodeId' no puede ser nulo en PowerNode");
    }
}
