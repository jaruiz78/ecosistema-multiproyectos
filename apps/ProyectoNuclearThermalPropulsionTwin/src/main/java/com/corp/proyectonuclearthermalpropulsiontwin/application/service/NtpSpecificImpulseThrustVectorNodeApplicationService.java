package com.corp.proyectonuclearthermalpropulsiontwin.application.service;

import com.corp.proyectonuclearthermalpropulsiontwin.domain.model.NtpSpecificImpulseThrustVectorNode;
import com.corp.proyectonuclearthermalpropulsiontwin.domain.port.in.ManageNtpSpecificImpulseThrustVectorNodeUseCase;
import com.corp.proyectonuclearthermalpropulsiontwin.domain.port.out.NtpSpecificImpulseThrustVectorNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de NtpSpecificImpulseThrustVectorNode.
 */
@Service
public class NtpSpecificImpulseThrustVectorNodeApplicationService implements ManageNtpSpecificImpulseThrustVectorNodeUseCase {

    private final NtpSpecificImpulseThrustVectorNodeRepositoryPort repositoryPort;

    public NtpSpecificImpulseThrustVectorNodeApplicationService(NtpSpecificImpulseThrustVectorNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public NtpSpecificImpulseThrustVectorNode createNtpSpecificImpulseThrustVectorNode(String tenantId, String title, double value) {
        NtpSpecificImpulseThrustVectorNode entity = new NtpSpecificImpulseThrustVectorNode(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<NtpSpecificImpulseThrustVectorNode> findNtpSpecificImpulseThrustVectorNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public NtpSpecificImpulseThrustVectorNode processOptimization(String id, String tenantId) {
        NtpSpecificImpulseThrustVectorNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        NtpSpecificImpulseThrustVectorNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
