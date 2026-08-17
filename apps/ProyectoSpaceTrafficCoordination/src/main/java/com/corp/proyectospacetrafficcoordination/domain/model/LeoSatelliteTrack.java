package com.corp.proyectospacetrafficcoordination.domain.model;

import java.io.Serializable;

/**
 * Traza orbital de satélite o fragmento de basura espacial en órbita baja (LEO).
 */
public record LeoSatelliteTrack(
        String noradCatalogId,
        String objectName,
        double semiMajorAxisKm,
        double eccentricity,
        double inclinationDeg,
        double rightAscensionDeg,
        double perigeeAltitudeKm,
        double collisionProbabilityPc,
        OperationalStatus status
) implements Serializable {

    public enum OperationalStatus {
        ACTIVE,
        DECOMMISSIONED,
        MANEUVERING,
        REENTRY_DECAY
    }

    public static LeoSatelliteTrack createActive(String noradId, String name, double altitudeKm, double incDeg) {
        double rEarth = 6378.137;
        return new LeoSatelliteTrack(noradId, name, rEarth + altitudeKm, 0.001, incDeg, 45.0, altitudeKm, 1e-7, OperationalStatus.ACTIVE);
    }

    public LeoSatelliteTrack evaluateConjunction(LeoSatelliteTrack other) {
        double distKm = Math.abs(this.perigeeAltitudeKm() - other.perigeeAltitudeKm());
        double pc = distKm < 5.0 ? Math.exp(-distKm / 0.5) * 1e-2 : 1e-7;

        OperationalStatus newStatus = pc > 1e-4 ? OperationalStatus.MANEUVERING : this.status;
        return new LeoSatelliteTrack(noradCatalogId, objectName, semiMajorAxisKm, eccentricity, inclinationDeg, rightAscensionDeg, perigeeAltitudeKm, pc, newStatus);
    }
}
