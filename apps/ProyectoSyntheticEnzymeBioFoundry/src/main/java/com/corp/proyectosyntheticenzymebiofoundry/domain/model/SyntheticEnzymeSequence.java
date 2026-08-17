package com.corp.proyectosyntheticenzymebiofoundry.domain.model;

import java.io.Serializable;

/**
 * Enzima diseñada de novo mediante modelos de difusión de proteínas (ESM-Fold / AlphaFold)
 * para la escisión de enlaces carbono-flúor (C-F) ultra-resistentes en sustancias per- y polifluoroalquiladas (PFAS).
 */
public record SyntheticEnzymeSequence(
        String enzymeDesignId,
        String targetSubstrate, // PFAS, PET, NYLON_66
        String catalyticDomainMotif,
        double catalyticEfficiencyKcatKm, // M^-1 s^-1
        double meltingTemperatureTmCelsius,
        EnzymeViability viability
) implements Serializable {

    public enum EnzymeViability {
        HIGH_ACTIVITY_STABLE,
        MODERATE_ACTIVITY,
        MISFOLDED_UNSTABLE
    }

    public static SyntheticEnzymeSequence create(String id, String substrate) {
        boolean isPfas = "PFAS".equalsIgnoreCase(substrate);
        double kcatKm = isPfas ? 4.5e5 : 8.2e4;
        double tm = 68.5; // Termoestabilidad optimizada
        return new SyntheticEnzymeSequence(id, substrate, "HIS-ASP-SER-CATALYTIC-TRIAD", kcatKm, tm, EnzymeViability.HIGH_ACTIVITY_STABLE);
    }
}
