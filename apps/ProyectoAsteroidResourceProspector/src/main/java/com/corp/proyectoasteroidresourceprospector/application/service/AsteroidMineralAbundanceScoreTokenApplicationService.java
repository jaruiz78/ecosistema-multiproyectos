package com.corp.proyectoasteroidresourceprospector.application.service;

import com.corp.proyectoasteroidresourceprospector.domain.model.AsteroidMineralAbundanceScoreToken;
import com.corp.proyectoasteroidresourceprospector.domain.port.in.ManageAsteroidMineralAbundanceScoreTokenUseCase;
import com.corp.proyectoasteroidresourceprospector.domain.port.out.AsteroidMineralAbundanceScoreTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AsteroidMineralAbundanceScoreToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AsteroidMineralAbundanceScoreTokenApplicationService implements ManageAsteroidMineralAbundanceScoreTokenUseCase {

    private final AsteroidMineralAbundanceScoreTokenRepositoryPort repositoryPort;

    public AsteroidMineralAbundanceScoreTokenApplicationService(AsteroidMineralAbundanceScoreTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AsteroidMineralAbundanceScoreToken createAsteroidMineralAbundanceScoreToken(String tenantId, String title, double value) {
        AsteroidMineralAbundanceScoreToken entity = new AsteroidMineralAbundanceScoreToken(
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
    public Optional<AsteroidMineralAbundanceScoreToken> findAsteroidMineralAbundanceScoreTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AsteroidMineralAbundanceScoreToken processOptimization(String id, String tenantId) {
        AsteroidMineralAbundanceScoreToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AsteroidMineralAbundanceScoreToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
