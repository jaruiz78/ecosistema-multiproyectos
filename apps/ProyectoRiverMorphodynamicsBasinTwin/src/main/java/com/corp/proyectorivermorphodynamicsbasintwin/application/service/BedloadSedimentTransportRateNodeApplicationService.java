package com.corp.proyectorivermorphodynamicsbasintwin.application.service;

import com.corp.proyectorivermorphodynamicsbasintwin.domain.model.BedloadSedimentTransportRateNode;
import com.corp.proyectorivermorphodynamicsbasintwin.domain.port.in.ManageBedloadSedimentTransportRateNodeUseCase;
import com.corp.proyectorivermorphodynamicsbasintwin.domain.port.out.BedloadSedimentTransportRateNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BedloadSedimentTransportRateNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class BedloadSedimentTransportRateNodeApplicationService implements ManageBedloadSedimentTransportRateNodeUseCase {

    private final BedloadSedimentTransportRateNodeRepositoryPort repositoryPort;

    public BedloadSedimentTransportRateNodeApplicationService(BedloadSedimentTransportRateNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BedloadSedimentTransportRateNode createBedloadSedimentTransportRateNode(String tenantId, String title, double value) {
        BedloadSedimentTransportRateNode entity = new BedloadSedimentTransportRateNode(
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
    public Optional<BedloadSedimentTransportRateNode> findBedloadSedimentTransportRateNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BedloadSedimentTransportRateNode processOptimization(String id, String tenantId) {
        BedloadSedimentTransportRateNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BedloadSedimentTransportRateNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
