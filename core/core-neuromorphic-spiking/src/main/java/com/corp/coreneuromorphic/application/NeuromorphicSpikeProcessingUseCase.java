package com.corp.coreneuromorphic.application;

import com.corp.core.math.neuromorphic.LIFNeuronModel;
import com.corp.core.math.neuromorphic.SpikeTrainEvent;
import com.corp.coreneuromorphic.domain.NeuromorphicCoreEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class NeuromorphicSpikeProcessingUseCase {

    public NeuromorphicCoreEntity processSignalStream(String networkId, double[] inputCurrents, double durationMs, double dtMs) {
        LIFNeuronModel neuron = LIFNeuronModel.standard();
        List<SpikeTrainEvent> spikes = new ArrayList<>();
        double time = 0.0;
        int index = 0;

        while (time < durationMs && index < inputCurrents.length) {
            var result = neuron.step(inputCurrents[index], time, dtMs);
            neuron = result.updatedNeuron();
            if (result.firedSpike()) {
                spikes.add(new SpikeTrainEvent(0, time, result.spikeAmplitude()));
            }
            time += dtMs;
            index++;
        }

        double spikeRate = (spikes.size() / (durationMs / 1000.0));
        return new NeuromorphicCoreEntity(networkId, 1, spikeRate, spikeRate < 100.0);
    }
}
