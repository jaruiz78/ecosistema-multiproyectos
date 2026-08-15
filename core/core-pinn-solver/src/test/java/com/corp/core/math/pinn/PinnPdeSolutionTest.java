package com.corp.core.math.pinn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PinnPdeSolutionTest {

    @Test
    @DisplayName("Debe calcular perfil de Saint-Venant 1D con convergencia y residuo submilimétrico")
    void shouldSolveSaintVenantPde() {
        PinnPdeSolution sol = PinnPdeSolution.solveSaintVenant1D(8.5, 450.0, 25.0, 100);

        assertNotNull(sol);
        assertTrue(sol.converged());
        assertEquals(100, sol.primaryFieldValues().length);
        assertTrue(sol.residualErrorNorm() < 1e-3);
    }
}
