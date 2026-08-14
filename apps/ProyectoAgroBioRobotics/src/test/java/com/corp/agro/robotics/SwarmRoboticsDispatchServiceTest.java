package com.corp.agro.robotics;

import com.corp.agro.robotics.domain.BioDroneSwarmNode;
import com.corp.agro.robotics.domain.SwarmRoboticsDispatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SwarmRoboticsDispatchServiceTest {

    private SwarmRoboticsDispatchService dispatchService;

    @BeforeEach
    void setUp() {
        dispatchService = new SwarmRoboticsDispatchService();
    }

    @Test
    void testDispatchPollinationMission() {
        var d1 = new BioDroneSwarmNode("DRONE_AGRO_01", "88390b1627fffff", 2.5, 85.0, 50.0, true, 350);
        var d2 = new BioDroneSwarmNode("DRONE_AGRO_02", "88390b1627fffff", 2.0, 90.0, 50.0, true, 420);
        var dLowBattery = new BioDroneSwarmNode("DRONE_AGRO_03", "88390b1627fffff", 0.0, 10.0, 0.0, false, 0);

        var plan = dispatchService.dispatchPollinationMission("MIS_POLLINATE_01", "PARCEL_ALMONDS_24", List.of(d1, d2, dLowBattery));

        assertNotNull(plan);
        assertEquals("MIS_POLLINATE_01", plan.missionId());
        assertEquals("PARCEL_ALMONDS_24", plan.targetParcelId());
        assertEquals(2, plan.activeDronesCount(), "Solo los drones con batería >= 20% deben activarse");
        assertEquals(770, plan.totalFlowersPollinated());
        assertEquals(0.50, plan.coverageHectares(), 0.01);
    }
}
