package com.corp.proyectocontextcacheaiorchestrator.application.service;

import com.corp.proyectocontextcacheaiorchestrator.domain.model.AiContextCacheSessionToken;
import com.corp.proyectocontextcacheaiorchestrator.domain.port.in.ManageAiContextCacheSessionTokenUseCase;
import com.corp.proyectocontextcacheaiorchestrator.domain.port.out.AiContextCacheSessionTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AiContextCacheSessionToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AiContextCacheSessionTokenApplicationService implements ManageAiContextCacheSessionTokenUseCase {

    private final AiContextCacheSessionTokenRepositoryPort repositoryPort;

    public AiContextCacheSessionTokenApplicationService(AiContextCacheSessionTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AiContextCacheSessionToken createAiContextCacheSessionToken(String tenantId, String title, double value) {
        AiContextCacheSessionToken entity = new AiContextCacheSessionToken(
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
    public Optional<AiContextCacheSessionToken> findAiContextCacheSessionTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AiContextCacheSessionToken processOptimization(String id, String tenantId) {
        AiContextCacheSessionToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AiContextCacheSessionToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
