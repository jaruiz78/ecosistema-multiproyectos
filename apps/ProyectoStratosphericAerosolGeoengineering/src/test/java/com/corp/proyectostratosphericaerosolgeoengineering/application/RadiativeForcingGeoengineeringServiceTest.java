package com.corp.proyectostratosphericaerosolgeoengineering.application;

import com.corp.proyectostratosphericaerosolgeoengineering.application.service.RadiativeForcingGeoengineeringService;
import com.corp.proyectostratosphericaerosolgeoengineering.infrastructure.adapter.out.persistence.InMemoryAerosolPlumeRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RadiativeForcingGeoengineeringServiceTest {

    @Test
    @DisplayName("Debe planificar inyección estratosférica y persistir estado")
    void testPlanStratosphericInjection() {
        var repo = new InMemoryAerosolPlumeRepositoryAdapter();
        var service = new RadiativeForcingGeoengineeringService(repo);

        var plume = service.planStratosphericInjection("INJ-001", 22.0, 2.5);

        assertNotNull(plume);
        assertEquals("INJ-001", plume.injectionId());
        assertTrue(plume.radiativeForcingWattsPerM2() < 0.0);
    }
}
