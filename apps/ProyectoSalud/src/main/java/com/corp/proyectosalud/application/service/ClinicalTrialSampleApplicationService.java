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
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
