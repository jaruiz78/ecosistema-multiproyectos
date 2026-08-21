package com.corp.proyectorecursivesnarkverifier.application.service;

import com.corp.proyectorecursivesnarkverifier.domain.model.Halo2ProofAggregationBatchToken;
import com.corp.proyectorecursivesnarkverifier.domain.port.in.ManageHalo2ProofAggregationBatchTokenUseCase;
import com.corp.proyectorecursivesnarkverifier.domain.port.out.Halo2ProofAggregationBatchTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de Halo2ProofAggregationBatchToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class Halo2ProofAggregationBatchTokenApplicationService implements ManageHalo2ProofAggregationBatchTokenUseCase {

    private final Halo2ProofAggregationBatchTokenRepositoryPort repositoryPort;

    public Halo2ProofAggregationBatchTokenApplicationService(Halo2ProofAggregationBatchTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Halo2ProofAggregationBatchToken createHalo2ProofAggregationBatchToken(String tenantId, String title, double value) {
        Halo2ProofAggregationBatchToken entity = new Halo2ProofAggregationBatchToken(
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
    public Optional<Halo2ProofAggregationBatchToken> findHalo2ProofAggregationBatchTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Halo2ProofAggregationBatchToken processOptimization(String id, String tenantId) {
        Halo2ProofAggregationBatchToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Halo2ProofAggregationBatchToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
