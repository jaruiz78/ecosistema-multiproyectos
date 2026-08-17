package com.corp.proyectospacetrafficcoordination.domain;

import com.corp.proyectospacetrafficcoordination.domain.model.LeoSatelliteTrack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeoSatelliteTrackTest {

    @Test
    @DisplayName("Debe detectar riesgo de conjunción y cambiar estado a MANEUVERING")
    void testConjunctionRiskDetection() {
        LeoSatelliteTrack sat = LeoSatelliteTrack.createActive("SAT-LEO-001", "OBS-SAT-A", 550.0, 53.0);
        LeoSatelliteTrack debris = LeoSatelliteTrack.createActive("DEBRIS-9999", "DEBRIS-FRAG", 550.2, 53.0);

        var assessed = sat.evaluateConjunction(debris);

        assertTrue(assessed.collisionProbabilityPc() > 1e-4);
        assertEquals(LeoSatelliteTrack.OperationalStatus.MANEUVERING, assessed.status());
    }
}
