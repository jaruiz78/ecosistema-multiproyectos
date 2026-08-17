package com.corp.proyectoenergia.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Segundo Agregado de Dominio Estratégico: GridSubstationNode.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion">FACULTAD_V: Gemelo Digital PEPS, EnKF & Física</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record GridSubstationNode(
    String substationId, String gridZone, double nominalCapacityKw, double currentLoadKw,
    Instant timestamp
) {
    public GridSubstationNode {
        Objects.requireNonNull(timestamp, "El timestamp es obligatorio");
        if (!(nominalCapacityKw > 0.0 && currentLoadKw >= 0.0)) {
            throw new IllegalArgumentException("Violación de invariante de negocio en GridSubstationNode");
        }
    }
}
