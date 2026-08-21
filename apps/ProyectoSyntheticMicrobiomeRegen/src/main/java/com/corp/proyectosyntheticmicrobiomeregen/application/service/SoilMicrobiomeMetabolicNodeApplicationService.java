package com.corp.proyectosyntheticmicrobiomeregen.application.service;

import com.corp.proyectosyntheticmicrobiomeregen.domain.model.SoilMicrobiomeMetabolicNode;
import com.corp.proyectosyntheticmicrobiomeregen.domain.port.in.ManageSoilMicrobiomeMetabolicNodeUseCase;
import com.corp.proyectosyntheticmicrobiomeregen.domain.port.out.SoilMicrobiomeMetabolicNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SoilMicrobiomeMetabolicNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SoilMicrobiomeMetabolicNodeApplicationService implements ManageSoilMicrobiomeMetabolicNodeUseCase {

    private final SoilMicrobiomeMetabolicNodeRepositoryPort repositoryPort;

    public SoilMicrobiomeMetabolicNodeApplicationService(SoilMicrobiomeMetabolicNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SoilMicrobiomeMetabolicNode createSoilMicrobiomeMetabolicNode(String tenantId, String title, double value) {
        SoilMicrobiomeMetabolicNode entity = new SoilMicrobiomeMetabolicNode(
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
    public Optional<SoilMicrobiomeMetabolicNode> findSoilMicrobiomeMetabolicNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SoilMicrobiomeMetabolicNode processOptimization(String id, String tenantId) {
        SoilMicrobiomeMetabolicNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SoilMicrobiomeMetabolicNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
