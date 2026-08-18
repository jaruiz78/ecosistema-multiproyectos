package com.corp.proyectospintronicterahertzemitter.domain.port.in;

import com.corp.proyectospintronicterahertzemitter.domain.model.SpintronicThzPulseWaveformNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSpintronicThzPulseWaveformNodeUseCase {
    SpintronicThzPulseWaveformNode createSpintronicThzPulseWaveformNode(String tenantId, String title, double value);
    Optional<SpintronicThzPulseWaveformNode> findSpintronicThzPulseWaveformNodeById(String id, String tenantId);
    SpintronicThzPulseWaveformNode processOptimization(String id, String tenantId);
}
