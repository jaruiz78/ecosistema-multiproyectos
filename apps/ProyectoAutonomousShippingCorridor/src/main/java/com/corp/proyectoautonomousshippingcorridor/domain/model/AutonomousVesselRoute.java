package com.corp.proyectoautonomousshippingcorridor.domain.model;

import java.io.Serializable;

/**
 * Ruta de navegación marítima autónoma compatible con reglamento COLREGs y cartas náuticas S-100.
 */
public record AutonomousVesselRoute(
        String imoVesselNumber,
        String vesselName,
        double headingDeg,
        double speedKnots,
        double draftMeters,
        NavigationMode mode
) implements Serializable {

    public enum NavigationMode {
        AUTONOMOUS_TRACK_KEEPING,
        COLREGS_AVOIDANCE_STARBOARD,
        PORT_APPROACH_PILOT
    }

    public static AutonomousVesselRoute create(String imo, String name) {
        return new AutonomousVesselRoute(imo, name, 90.0, 18.0, 14.5, NavigationMode.AUTONOMOUS_TRACK_KEEPING);
    }

    public AutonomousVesselRoute executeColregsManeuver(double obstacleBearingDeg, double distanceNauticalMiles) {
        if (distanceNauticalMiles < 3.0 && Math.abs(headingDeg - obstacleBearingDeg) < 30.0) {
            // Riesgo de abordaje -> Maniobra a estribor
            double newHeading = (headingDeg + 25.0) % 360.0;
            return new AutonomousVesselRoute(imoVesselNumber, vesselName, newHeading, speedKnots * 0.9, draftMeters, NavigationMode.COLREGS_AVOIDANCE_STARBOARD);
        }
        return this;
    }
}
