package com.corp.proyectosalud.application.service;

import com.corp.proyectosalud.domain.model.ClinicalTrialSample;
import com.corp.proyectosalud.domain.port.in.ManageClinicalTrialSampleUseCase;
import com.corp.proyectosalud.domain.port.out.ClinicalTrialSampleRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ClinicalTrialSample.
 */
@Service
public class ClinicalTrialSampleApplicationService implements ManageClinicalTrialSampleUseCase {

    private final ClinicalTrialSampleRepositoryPort repositoryPort;

    public ClinicalTrialSampleApplicationService(ClinicalTrialSampleRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ClinicalTrialSample createClinicalTrialSample(String tenantId, String title, double value) {
        ClinicalTrialSample entity = new ClinicalTrialSample(
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
    public Optional<ClinicalTrialSample> findClinicalTrialSampleById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ClinicalTrialSample processOptimization(String id, String tenantId) {
        ClinicalTrialSample existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ClinicalTrialSample optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
