package com.corp.proyectoplantelectromestressalert.application.service;

import com.corp.proyectoplantelectromestressalert.domain.model.PlantBiopotentialSpikeNode;
import com.corp.proyectoplantelectromestressalert.domain.port.in.ManagePlantBiopotentialSpikeNodeUseCase;
import com.corp.proyectoplantelectromestressalert.domain.port.out.PlantBiopotentialSpikeNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PlantBiopotentialSpikeNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PlantBiopotentialSpikeNodeApplicationService implements ManagePlantBiopotentialSpikeNodeUseCase {

    private final PlantBiopotentialSpikeNodeRepositoryPort repositoryPort;

    public PlantBiopotentialSpikeNodeApplicationService(PlantBiopotentialSpikeNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PlantBiopotentialSpikeNode createPlantBiopotentialSpikeNode(String tenantId, String title, double value) {
        PlantBiopotentialSpikeNode entity = new PlantBiopotentialSpikeNode(
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
    public Optional<PlantBiopotentialSpikeNode> findPlantBiopotentialSpikeNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PlantBiopotentialSpikeNode processOptimization(String id, String tenantId) {
        PlantBiopotentialSpikeNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PlantBiopotentialSpikeNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
