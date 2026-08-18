package com.corp.proyectospintronicterahertzemitter.domain.port.out;

import com.corp.proyectospintronicterahertzemitter.domain.model.SpintronicThzPulseWaveformNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SpintronicThzPulseWaveformNodeRepositoryPort {
    SpintronicThzPulseWaveformNode save(SpintronicThzPulseWaveformNode entity);
    Optional<SpintronicThzPulseWaveformNode> findById(String id, String tenantId);
}
