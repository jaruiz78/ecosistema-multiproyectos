package com.corp.core.math.neuromorphic;

import java.io.Serializable;

/**
 * Evento temporal discreto de disparo de potencial de acción (Spike).
 */
public record SpikeTrainEvent(
        int neuronIndex,
        double timestampMs,
        double amplitude
) implements Serializable {}
