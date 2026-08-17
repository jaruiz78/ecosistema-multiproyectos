package com.corp.proyectonuclearfusionstellarator.application;

import com.corp.proyectonuclearfusionstellarator.application.service.StellaratorPlasmaEquilibriumService;
import com.corp.proyectonuclearfusionstellarator.infrastructure.adapter.InMemoryStellaratorRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StellaratorPlasmaEquilibriumServiceTest {

    @Test
    @DisplayName("Debe optimizar geometría magnética helicoidal y persistir en repositorio")
    void testOptimizeMagneticGeometry() {
        var repo = new InMemoryStellaratorRepositoryAdapter();
        var service = new StellaratorPlasmaEquilibriumService(repo);

        var result = service.optimizeMagneticGeometry("W7X-STELLARATOR-01", 50, 3.0, 0.85);

        assertNotNull(result);
        assertEquals("W7X-STELLARATOR-01", result.reactorId());
        assertEquals(50, result.numberOfNonPlanarCoils());
        assertEquals(3.0, result.magneticFieldStrengthTesla());
        assertTrue(result.plasmaBetaPercentage() > 0.0);

        assertTrue(repo.findById("W7X-STELLARATOR-01").isPresent());
    }
}
