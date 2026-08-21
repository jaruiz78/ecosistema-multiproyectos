package com.corp.proyectosalud.application.service;

import com.corp.proyectosalud.domain.model.ClinicalTrialSubject;
import com.corp.proyectosalud.domain.port.in.ManageClinicalTrialSubjectUseCase;
import com.corp.proyectosalud.domain.port.out.ClinicalTrialSubjectRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ClinicalTrialSubject.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ClinicalTrialSubjectApplicationService implements ManageClinicalTrialSubjectUseCase {

    private final ClinicalTrialSubjectRepositoryPort repositoryPort;

    public ClinicalTrialSubjectApplicationService(ClinicalTrialSubjectRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ClinicalTrialSubject createClinicalTrialSubject(String tenantId, String title, double value) {
        ClinicalTrialSubject entity = new ClinicalTrialSubject(
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
    public Optional<ClinicalTrialSubject> findClinicalTrialSubjectById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ClinicalTrialSubject processOptimization(String id, String tenantId) {
        ClinicalTrialSubject existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ClinicalTrialSubject optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
