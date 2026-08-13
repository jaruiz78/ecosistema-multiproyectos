package com.proyecto.maritime.application;

import com.proyecto.maritime.domain.VesselBerthAssignment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortBerthOptimizerServiceTest {

    @Test
    void testOptimizeBerthSlotSuccess() {
        PortBerthOptimizerService service = new PortBerthOptimizerService();
        VesselBerthAssignment vessel = new VesselBerthAssignment("vessel_maersk_01", "UNASSIGNED", 1200, 0, false);

        VesselBerthAssignment allocated = service.optimizeBerthSlot(vessel, "BERTH_NORTH_03", 100.0); // 12 horas = 720 min

        assertNotNull(allocated);
        assertTrue(allocated.allocated());
        assertEquals("BERTH_NORTH_03", allocated.berthId());
        assertEquals(720, allocated.estimatedTurnaroundMinutes());
    }
}
