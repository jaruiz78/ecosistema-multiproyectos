package com.corp.core.math.neuromorphic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo analítico de neurona Leaky Integrate-and-Fire (LIF) con decaimiento exponencial
 * y refractariedad estricta en $O(1)$ por tick temporal.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 */
public record LIFNeuronModel(
        double membranePotentialV,
        double restingPotentialV,
        double thresholdPotentialV,
        double resetPotentialV,
        double tauMembraneMs,
        double refractoryPeriodMs,
        double lastSpikeTimeMs
) implements Serializable {

    public static LIFNeuronModel standard() {
        return new LIFNeuronModel(-70.0, -70.0, -55.0, -75.0, 20.0, 2.0, -100.0);
    }

    /**
     * Integra la corriente sináptica de entrada I_syn en un intervalo dt y calcula si ocurre un disparo.
     */
    public StepResult step(double inputCurrent, double currentTimeMs, double dtMs) {
        if (currentTimeMs - lastSpikeTimeMs < refractoryPeriodMs) {
            // Período refractario absoluto
            return new StepResult(new LIFNeuronModel(resetPotentialV, restingPotentialV, thresholdPotentialV, resetPotentialV, tauMembraneMs, refractoryPeriodMs, lastSpikeTimeMs), false, 0.0);
        }

        // dV/dt = -(V - V_rest)/tau + I_syn * R_m
        double decay = Math.exp(-dtMs / tauMembraneMs);
        double membraneResistance = 10.0; // MegaOhms
        double newPotential = restingPotentialV + (membranePotentialV - restingPotentialV) * decay + (inputCurrent * membraneResistance) * (1.0 - decay);

        if (newPotential >= thresholdPotentialV) {
            // Disparo de spike
            LIFNeuronModel fired = new LIFNeuronModel(resetPotentialV, restingPotentialV, thresholdPotentialV, resetPotentialV, tauMembraneMs, refractoryPeriodMs, currentTimeMs);
            return new StepResult(fired, true, 1.0);
        } else {
            LIFNeuronModel subthreshold = new LIFNeuronModel(newPotential, restingPotentialV, thresholdPotentialV, resetPotentialV, tauMembraneMs, refractoryPeriodMs, lastSpikeTimeMs);
            return new StepResult(subthreshold, false, 0.0);
        }
    }

    public record StepResult(
            LIFNeuronModel updatedNeuron,
            boolean firedSpike,
            double spikeAmplitude
    ) {}
}
