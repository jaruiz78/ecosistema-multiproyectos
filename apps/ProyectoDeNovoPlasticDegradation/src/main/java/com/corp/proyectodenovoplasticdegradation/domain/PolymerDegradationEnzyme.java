package com.corp.proyectodenovoplasticdegradation.domain;

import java.io.Serializable;

/**
 * Representa una enzima bioingenieril para la despolimerización de microplásticos y PET.
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
