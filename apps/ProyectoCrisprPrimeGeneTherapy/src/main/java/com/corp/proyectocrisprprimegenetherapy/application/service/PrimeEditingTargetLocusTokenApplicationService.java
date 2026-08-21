package com.corp.proyectocrisprprimegenetherapy.application.service;

import com.corp.proyectocrisprprimegenetherapy.domain.model.PrimeEditingTargetLocusToken;
import com.corp.proyectocrisprprimegenetherapy.domain.port.in.ManagePrimeEditingTargetLocusTokenUseCase;
import com.corp.proyectocrisprprimegenetherapy.domain.port.out.PrimeEditingTargetLocusTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PrimeEditingTargetLocusToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PrimeEditingTargetLocusTokenApplicationService implements ManagePrimeEditingTargetLocusTokenUseCase {

    private final PrimeEditingTargetLocusTokenRepositoryPort repositoryPort;

    public PrimeEditingTargetLocusTokenApplicationService(PrimeEditingTargetLocusTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PrimeEditingTargetLocusToken createPrimeEditingTargetLocusToken(String tenantId, String title, double value) {
        PrimeEditingTargetLocusToken entity = new PrimeEditingTargetLocusToken(
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
    public Optional<PrimeEditingTargetLocusToken> findPrimeEditingTargetLocusTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PrimeEditingTargetLocusToken processOptimization(String id, String tenantId) {
        PrimeEditingTargetLocusToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PrimeEditingTargetLocusToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
