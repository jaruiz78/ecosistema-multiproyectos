package com.corp.proyectoquantummaterialsgraphene.domain.model;

import java.io.Serializable;

/**
 * Modelo de heteroestructura 2D de grafeno bicapa con ángulo mágico (\(\theta \approx 1.1^\circ\))
 * para simulación de bandas planas y superconductividad no convencional de pares de Cooper.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record GrapheneHeterostructure(
        String sampleId,
        double twistAngleDeg,
        double criticalTemperatureKelvin,
        double fermiVelocityMPerS,
        SuperconductingPhase phase
) implements Serializable {

    public enum SuperconductingPhase {
        CORRELATED_INSULATOR,
        UNCONVENTIONAL_SUPERCONDUCTING,
        METALLIC_FERMI_LIQUID
    }

    public static GrapheneHeterostructure create(String sampleId, double angleDeg) {
        boolean isMagicAngle = Math.abs(angleDeg - 1.1) < 0.05;
        double tc = isMagicAngle ? 1.7 : 0.0;
        double vf = isMagicAngle ? 1e4 : 1e6;
        SuperconductingPhase ph = isMagicAngle ? SuperconductingPhase.UNCONVENTIONAL_SUPERCONDUCTING : SuperconductingPhase.METALLIC_FERMI_LIQUID;
        return new GrapheneHeterostructure(sampleId, angleDeg, tc, vf, ph);
    }
}
