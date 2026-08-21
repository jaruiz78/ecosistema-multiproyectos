package com.corp.proyectoliquidmetalsoftelectronics.application.service;

import com.corp.proyectoliquidmetalsoftelectronics.domain.model.LiquidMetalConductorTraceBatch;
import com.corp.proyectoliquidmetalsoftelectronics.domain.port.in.ManageLiquidMetalConductorTraceBatchUseCase;
import com.corp.proyectoliquidmetalsoftelectronics.domain.port.out.LiquidMetalConductorTraceBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de LiquidMetalConductorTraceBatch.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class LiquidMetalConductorTraceBatchApplicationService implements ManageLiquidMetalConductorTraceBatchUseCase {

    private final LiquidMetalConductorTraceBatchRepositoryPort repositoryPort;

    public LiquidMetalConductorTraceBatchApplicationService(LiquidMetalConductorTraceBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public LiquidMetalConductorTraceBatch createLiquidMetalConductorTraceBatch(String tenantId, String title, double value) {
        LiquidMetalConductorTraceBatch entity = new LiquidMetalConductorTraceBatch(
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
    public Optional<LiquidMetalConductorTraceBatch> findLiquidMetalConductorTraceBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public LiquidMetalConductorTraceBatch processOptimization(String id, String tenantId) {
        LiquidMetalConductorTraceBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        LiquidMetalConductorTraceBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
