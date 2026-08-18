package com.corp.proyectohealthfederatedclinical.application.service;

import com.corp.proyectohealthfederatedclinical.domain.model.ClinicalTrialEnclave;
import com.corp.proyectohealthfederatedclinical.domain.port.in.ManageClinicalTrialEnclaveUseCase;
import com.corp.proyectohealthfederatedclinical.domain.port.out.ClinicalTrialEnclaveRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ClinicalTrialEnclave.
 */
@Service
public class ClinicalTrialEnclaveApplicationService implements ManageClinicalTrialEnclaveUseCase {

    private final ClinicalTrialEnclaveRepositoryPort repositoryPort;

    public ClinicalTrialEnclaveApplicationService(ClinicalTrialEnclaveRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ClinicalTrialEnclave createClinicalTrialEnclave(String tenantId, String title, double value) {
        ClinicalTrialEnclave entity = new ClinicalTrialEnclave(
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
    public Optional<ClinicalTrialEnclave> findClinicalTrialEnclaveById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ClinicalTrialEnclave processOptimization(String id, String tenantId) {
        ClinicalTrialEnclave existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ClinicalTrialEnclave optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
