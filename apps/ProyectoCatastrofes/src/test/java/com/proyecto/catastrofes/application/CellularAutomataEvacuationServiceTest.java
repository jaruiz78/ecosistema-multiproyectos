package com.proyecto.catastrofes.application;

import com.proyecto.catastrofes.domain.EvacuationZoneNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellularAutomataEvacuationServiceTest {

    @Test
    void testStepEvacuationProgress() {
        CellularAutomataEvacuationService service = new CellularAutomataEvacuationService();
        EvacuationZoneNode zone = new EvacuationZoneNode("zone_alpha", "8828308281fffff", 500, 75.0, false);

        EvacuationZoneNode stepped = service.stepEvacuation(zone, 150);

        assertNotNull(stepped);
        assertEquals(350, stepped.currentEvacueeCount());
    }
}
