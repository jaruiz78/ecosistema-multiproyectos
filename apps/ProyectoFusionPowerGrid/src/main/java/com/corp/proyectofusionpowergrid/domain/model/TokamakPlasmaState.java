package com.corp.proyectofusionpowergrid.domain.model;

import java.io.Serializable;

/**
 * Estado de confinamiento magnético de plasma en reactor Tokamak / Stellarator.
 */
public record TokamakPlasmaState(
        String reactorId,
        double electronTemperatureKeV,
        double ionDensityM3,
        double toroidalMagneticFieldTesla,
        double betaNormalized, // Límite de Troyon de inestabilidad MHD
        ConfinementQuality quality
) implements Serializable {

    public enum ConfinementQuality {
        L_MODE,
        H_MODE,
        DISRUPTION_WARNING
    }

    public static TokamakPlasmaState create(String reactorId, double bFieldTesla) {
        return new TokamakPlasmaState(reactorId, 15.0, 1.2e20, bFieldTesla, 2.2, ConfinementQuality.H_MODE);
    }

    public TokamakPlasmaState updateMhdParameters(double newTempKeV, double newBeta) {
        ConfinementQuality q = newBeta > 3.5 ? ConfinementQuality.DISRUPTION_WARNING : ConfinementQuality.H_MODE;
        return new TokamakPlasmaState(reactorId, newTempKeV, ionDensityM3, toroidalMagneticFieldTesla, newBeta, q);
    }
}
