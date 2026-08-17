package com.corp.proyectocislunarspacelogistics.application;

import com.corp.proyectocislunarspacelogistics.application.service.CislunarOrbitOptimizationService;
import com.corp.proyectocislunarspacelogistics.infrastructure.adapter.out.persistence.InMemoryLagrangeTrajectoryRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CislunarOrbitOptimizationServiceTest {

    @Test
    @DisplayName("Debe optimizar trayectoria cislunar y persistir registro de misión")
    void testPlanCislunarTransfer() {
        var repo = new InMemoryLagrangeTrajectoryRepositoryAdapter();
        var service = new CislunarOrbitOptimizationService(repo);

        var traj = service.planCislunarTransfer("LUNAR-CARGO-01", "L1");

        assertNotNull(traj);
        assertEquals("LUNAR-CARGO-01", traj.missionId());
        assertEquals("L1", traj.destinationLagrangePoint());
    }
}
