package com.corp.core.math.neuromorphic;

import java.io.Serializable;

/**
 * Evento temporal discreto de disparo de potencial de acción (Spike).
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public record SpikeTrainEvent(
        int neuronIndex,
        double timestampMs,
        double amplitude
) implements Serializable {}
