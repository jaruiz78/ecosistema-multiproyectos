package com.corp.proyectoagua.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Segundo Agregado de Dominio Estratégico: WaterPressureValve.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public record WaterPressureValve(
    String valveId, String pipelineSegment, double pressureBar, double flowRateLps,
    Instant timestamp
) {
    public WaterPressureValve {
        Objects.requireNonNull(timestamp, "El timestamp es obligatorio");
        if (!(pressureBar >= 0.0 && flowRateLps >= 0.0)) {
            throw new IllegalArgumentException("Violación de invariante de negocio en WaterPressureValve");
        }
    }
}
