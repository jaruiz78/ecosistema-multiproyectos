package com.corp.proyectoecotasasoberanatax.application.service;

import com.corp.proyectoecotasasoberanatax.domain.model.SovereignEcoTaxAssessment;
import com.corp.proyectoecotasasoberanatax.domain.port.in.ManageSovereignEcoTaxAssessmentUseCase;
import com.corp.proyectoecotasasoberanatax.domain.port.out.SovereignEcoTaxAssessmentRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SovereignEcoTaxAssessment.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SovereignEcoTaxAssessmentApplicationService implements ManageSovereignEcoTaxAssessmentUseCase {

    private final SovereignEcoTaxAssessmentRepositoryPort repositoryPort;

    public SovereignEcoTaxAssessmentApplicationService(SovereignEcoTaxAssessmentRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SovereignEcoTaxAssessment createSovereignEcoTaxAssessment(String tenantId, String title, double value) {
        SovereignEcoTaxAssessment entity = new SovereignEcoTaxAssessment(
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
    public Optional<SovereignEcoTaxAssessment> findSovereignEcoTaxAssessmentById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SovereignEcoTaxAssessment processOptimization(String id, String tenantId) {
        SovereignEcoTaxAssessment existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SovereignEcoTaxAssessment optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
