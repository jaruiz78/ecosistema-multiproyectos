package com.corp.proyectobasaltcarbonmineralizationdac.application.service;

import com.corp.proyectobasaltcarbonmineralizationdac.domain.model.BasaltCarbonSequestrationWellToken;
import com.corp.proyectobasaltcarbonmineralizationdac.domain.port.in.ManageBasaltCarbonSequestrationWellTokenUseCase;
import com.corp.proyectobasaltcarbonmineralizationdac.domain.port.out.BasaltCarbonSequestrationWellTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BasaltCarbonSequestrationWellToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class BasaltCarbonSequestrationWellTokenApplicationService implements ManageBasaltCarbonSequestrationWellTokenUseCase {

    private final BasaltCarbonSequestrationWellTokenRepositoryPort repositoryPort;

    public BasaltCarbonSequestrationWellTokenApplicationService(BasaltCarbonSequestrationWellTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BasaltCarbonSequestrationWellToken createBasaltCarbonSequestrationWellToken(String tenantId, String title, double value) {
        BasaltCarbonSequestrationWellToken entity = new BasaltCarbonSequestrationWellToken(
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
    public Optional<BasaltCarbonSequestrationWellToken> findBasaltCarbonSequestrationWellTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BasaltCarbonSequestrationWellToken processOptimization(String id, String tenantId) {
        BasaltCarbonSequestrationWellToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BasaltCarbonSequestrationWellToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
