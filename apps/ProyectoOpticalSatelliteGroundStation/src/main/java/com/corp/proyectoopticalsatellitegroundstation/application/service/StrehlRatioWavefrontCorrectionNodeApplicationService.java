package com.corp.proyectoopticalsatellitegroundstation.application.service;

import com.corp.proyectoopticalsatellitegroundstation.domain.model.StrehlRatioWavefrontCorrectionNode;
import com.corp.proyectoopticalsatellitegroundstation.domain.port.in.ManageStrehlRatioWavefrontCorrectionNodeUseCase;
import com.corp.proyectoopticalsatellitegroundstation.domain.port.out.StrehlRatioWavefrontCorrectionNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de StrehlRatioWavefrontCorrectionNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class StrehlRatioWavefrontCorrectionNodeApplicationService implements ManageStrehlRatioWavefrontCorrectionNodeUseCase {

    private final StrehlRatioWavefrontCorrectionNodeRepositoryPort repositoryPort;

    public StrehlRatioWavefrontCorrectionNodeApplicationService(StrehlRatioWavefrontCorrectionNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public StrehlRatioWavefrontCorrectionNode createStrehlRatioWavefrontCorrectionNode(String tenantId, String title, double value) {
        StrehlRatioWavefrontCorrectionNode entity = new StrehlRatioWavefrontCorrectionNode(
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
    public Optional<StrehlRatioWavefrontCorrectionNode> findStrehlRatioWavefrontCorrectionNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public StrehlRatioWavefrontCorrectionNode processOptimization(String id, String tenantId) {
        StrehlRatioWavefrontCorrectionNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        StrehlRatioWavefrontCorrectionNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
