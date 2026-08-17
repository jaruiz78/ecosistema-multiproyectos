package com.corp.proyectonuclearfusionstellarator.domain;

import java.io.Serializable;

/**
 * Representa la configuración geométrica de bobinas tridimensionales no planas y confinamiento helicoidal de plasma.
 */
public record StellaratorMagneticField(
        String reactorId,
        int numberOfNonPlanarCoils,
        double magneticFieldStrengthTesla,
        double rotationalTransformIota,
        double plasmaBetaPercentage
) implements Serializable {

    public static StellaratorMagneticField create(String id, int coils, double tesla, double iota) {
        double beta = (tesla > 0.0) ? (iota * 5.0) / tesla : 0.0;
        return new StellaratorMagneticField(id, coils, tesla, iota, beta);
    }
}
