package com.corp.proyectoautonomousshippingcorridor.application;

import com.corp.proyectoautonomousshippingcorridor.application.service.AutonomousMaritimeActorMeshService;
import com.corp.proyectoautonomousshippingcorridor.domain.model.AutonomousVesselRoute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutonomousMaritimeActorMeshServiceTest {

    @Test
    @DisplayName("Debe coordinar buque autónomo con maniobra COLREGs y registrar actor espacial H3")
    void testAutonomousMaritimeActorMeshSynergy() {
        var service = new AutonomousMaritimeActorMeshService();
        AutonomousVesselRoute vessel = AutonomousVesselRoute.create("IMO-9988776", "MEDITERRANEAN_AUTONOMOUS_FEEDER");

        var state = service.dispatchVesselInSpatialActorMesh(
                vessel,
                36.1408, // Estrecho de Gibraltar Lat
                -5.3536, // Long
                92.0,    // Rumbo obstáculo en proa
                1.8      // Distancia < 3.0 NM -> Riesgo de colisión
        );

        assertNotNull(state);
        assertEquals("IMO-9988776", state.imoVesselNumber());
        assertEquals(AutonomousVesselRoute.NavigationMode.COLREGS_AVOIDANCE_STARBOARD, state.navigationMode());
        assertTrue(state.actorMessageEpoch() >= 1);
    }
}
