package com.corp.proyectonuclearfusionstellarator.domain;

import java.io.Serializable;

/**
 * Representa la configuración geométrica de bobinas tridimensionales no planas y confinamiento helicoidal de plasma.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
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
