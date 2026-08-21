package com.corp.proyectodenovoplasticdegradation.domain;

import java.io.Serializable;

/**
 * Representa una enzima bioingenieril para la despolimerización de microplásticos y PET.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record PolymerDegradationEnzyme(
        String enzymeId,
        String targetPolymerType,
        double catalyticEfficiencyKcatKm,
        double optimalTemperatureCelsius,
        double degradationRateGramsPerHour
) implements Serializable {

    public static PolymerDegradationEnzyme create(String id, String polymer, double kcatKm, double temp) {
        double rate = kcatKm * 0.45 * (temp / 50.0);
        return new PolymerDegradationEnzyme(id, polymer, kcatKm, temp, rate);
    }
}
