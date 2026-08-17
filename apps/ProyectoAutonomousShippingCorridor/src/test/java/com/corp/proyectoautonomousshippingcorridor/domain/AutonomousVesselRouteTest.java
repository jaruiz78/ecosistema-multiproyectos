package com.corp.proyectoautonomousshippingcorridor.domain;

import com.corp.proyectoautonomousshippingcorridor.domain.model.AutonomousVesselRoute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutonomousVesselRouteTest {

    @Test
    @DisplayName("Debe ejecutar maniobra a estribor ante riesgo de colisión según COLREGs")
    void testColregsStarboardAvoidance() {
        AutonomousVesselRoute route = AutonomousVesselRoute.create("IMO-9876543", "PACIFIC_AUTONOMOUS_01");
        var maneuvered = route.executeColregsManeuver(95.0, 1.5); // Obstáculo a 1.5 millas en proa

        assertEquals(115.0, maneuvered.headingDeg(), 1e-3);
        assertEquals(AutonomousVesselRoute.NavigationMode.COLREGS_AVOIDANCE_STARBOARD, maneuvered.mode());
    }
}
