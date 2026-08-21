package com.corp.proyectobioagritrace.application.service;

import com.corp.proyectobioagritrace.domain.model.BioAgriGenomicSample;
import com.corp.proyectobioagritrace.domain.port.in.ManageBioAgriGenomicSampleUseCase;
import com.corp.proyectobioagritrace.domain.port.out.BioAgriGenomicSampleRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BioAgriGenomicSample.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class BioAgriGenomicSampleApplicationService implements ManageBioAgriGenomicSampleUseCase {

    private final BioAgriGenomicSampleRepositoryPort repositoryPort;

    public BioAgriGenomicSampleApplicationService(BioAgriGenomicSampleRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BioAgriGenomicSample createBioAgriGenomicSample(String tenantId, String title, double value) {
        BioAgriGenomicSample entity = new BioAgriGenomicSample(
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
    public Optional<BioAgriGenomicSample> findBioAgriGenomicSampleById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BioAgriGenomicSample processOptimization(String id, String tenantId) {
        BioAgriGenomicSample existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BioAgriGenomicSample optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
