package com.proyecto.vpp.application;

import com.proyecto.vpp.domain.DistributedEnergyResource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryDispatchServiceTest {

    @Test
    void testDispatchPeakLoadNormal() {
        BatteryDispatchService service = new BatteryDispatchService();
        DistributedEnergyResource battery = new DistributedEnergyResource("der_bat_001", "8828308281fffff", 100.0, 90.0, 50.0);

        DistributedEnergyResource dispatched = service.dispatchPeakLoad(battery, 25.0, 1.0); // 25 kWh

        assertNotNull(dispatched);
        assertEquals(65.0, dispatched.currentSocPercent(), 0.01);
    }
}
