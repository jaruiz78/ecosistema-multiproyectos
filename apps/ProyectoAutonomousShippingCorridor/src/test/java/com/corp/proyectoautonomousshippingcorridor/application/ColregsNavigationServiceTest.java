package com.corp.proyectoautonomousshippingcorridor.application;

import com.corp.proyectoautonomousshippingcorridor.application.service.ColregsNavigationService;
import com.corp.proyectoautonomousshippingcorridor.infrastructure.adapter.out.persistence.InMemoryVesselRouteRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColregsNavigationServiceTest {

    @Test
    @DisplayName("Debe evitar colisión y persistir ruta actualizada")
    void testAvoidCollision() {
        var repo = new InMemoryVesselRouteRepositoryAdapter();
        var service = new ColregsNavigationService(repo);

        var result = service.avoidCollision("IMO-001", 92.0, 2.0);

        assertNotNull(result);
        assertEquals("IMO-001", result.imoVesselNumber());
    }
}
