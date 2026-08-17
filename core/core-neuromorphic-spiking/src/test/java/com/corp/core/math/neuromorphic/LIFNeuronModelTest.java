package com.corp.core.math.neuromorphic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LIFNeuronModelTest {

    @Test
    @DisplayName("Debe integrar potencial sin disparo cuando la corriente está por debajo del umbral")
    void testSubthresholdIntegration() {
        LIFNeuronModel neuron = LIFNeuronModel.standard();
        var result = neuron.step(2.0, 0.0, 1.0);

        assertFalse(result.firedSpike());
        assertTrue(result.updatedNeuron().membranePotentialV() > -70.0);
        assertTrue(result.updatedNeuron().membranePotentialV() < -55.0);
    }

    @Test
    @DisplayName("Debe disparar spike al superar el umbral y entrar en periodo refractario")
    void testSpikeFiringAndRefractoriness() {
        LIFNeuronModel neuron = LIFNeuronModel.standard();
        // Inyectamos corriente fuerte para forzar potencial > -55.0 mV
        var result = neuron.step(50.0, 0.0, 1.0);

        assertTrue(result.firedSpike());
        assertEquals(-75.0, result.updatedNeuron().membranePotentialV(), 1e-3);

        // En t=0.5 ms dentro del periodo refractario (2ms), no debe disparar
        var refractoryResult = result.updatedNeuron().step(50.0, 0.5, 0.5);
        assertFalse(refractoryResult.firedSpike());
    }
}
