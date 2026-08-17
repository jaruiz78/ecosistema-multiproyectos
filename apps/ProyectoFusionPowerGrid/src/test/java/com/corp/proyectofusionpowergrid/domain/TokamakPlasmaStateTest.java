package com.corp.proyectofusionpowergrid.domain;

import com.corp.proyectofusionpowergrid.domain.model.TokamakPlasmaState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokamakPlasmaStateTest {

    @Test
    @DisplayName("Debe actualizar parámetros MHD y alertar disrupción si supera límite de Troyon")
    void testDisruptionAlertOnHighBeta() {
        TokamakPlasmaState state = TokamakPlasmaState.create("FUSION-REACTOR-01", 5.3);
        var updated = state.updateMhdParameters(22.0, 3.8); // Beta > 3.5

        assertEquals(22.0, updated.electronTemperatureKeV(), 1e-3);
        assertEquals(TokamakPlasmaState.ConfinementQuality.DISRUPTION_WARNING, updated.quality());
    }
}
