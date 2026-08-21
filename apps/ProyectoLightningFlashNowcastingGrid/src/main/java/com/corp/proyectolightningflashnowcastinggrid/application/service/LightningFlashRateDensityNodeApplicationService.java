package com.corp.proyectolightningflashnowcastinggrid.application.service;

import com.corp.proyectolightningflashnowcastinggrid.domain.model.LightningFlashRateDensityNode;
import com.corp.proyectolightningflashnowcastinggrid.domain.port.in.ManageLightningFlashRateDensityNodeUseCase;
import com.corp.proyectolightningflashnowcastinggrid.domain.port.out.LightningFlashRateDensityNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de LightningFlashRateDensityNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class LightningFlashRateDensityNodeApplicationService implements ManageLightningFlashRateDensityNodeUseCase {

    private final LightningFlashRateDensityNodeRepositoryPort repositoryPort;

    public LightningFlashRateDensityNodeApplicationService(LightningFlashRateDensityNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public LightningFlashRateDensityNode createLightningFlashRateDensityNode(String tenantId, String title, double value) {
        LightningFlashRateDensityNode entity = new LightningFlashRateDensityNode(
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
    public Optional<LightningFlashRateDensityNode> findLightningFlashRateDensityNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public LightningFlashRateDensityNode processOptimization(String id, String tenantId) {
        LightningFlashRateDensityNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        LightningFlashRateDensityNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
