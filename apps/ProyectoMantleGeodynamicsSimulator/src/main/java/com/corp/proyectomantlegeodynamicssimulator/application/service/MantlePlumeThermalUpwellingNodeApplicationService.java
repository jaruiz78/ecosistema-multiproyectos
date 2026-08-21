package com.corp.proyectomantlegeodynamicssimulator.application.service;

import com.corp.proyectomantlegeodynamicssimulator.domain.model.MantlePlumeThermalUpwellingNode;
import com.corp.proyectomantlegeodynamicssimulator.domain.port.in.ManageMantlePlumeThermalUpwellingNodeUseCase;
import com.corp.proyectomantlegeodynamicssimulator.domain.port.out.MantlePlumeThermalUpwellingNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MantlePlumeThermalUpwellingNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MantlePlumeThermalUpwellingNodeApplicationService implements ManageMantlePlumeThermalUpwellingNodeUseCase {

    private final MantlePlumeThermalUpwellingNodeRepositoryPort repositoryPort;

    public MantlePlumeThermalUpwellingNodeApplicationService(MantlePlumeThermalUpwellingNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MantlePlumeThermalUpwellingNode createMantlePlumeThermalUpwellingNode(String tenantId, String title, double value) {
        MantlePlumeThermalUpwellingNode entity = new MantlePlumeThermalUpwellingNode(
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
    public Optional<MantlePlumeThermalUpwellingNode> findMantlePlumeThermalUpwellingNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MantlePlumeThermalUpwellingNode processOptimization(String id, String tenantId) {
        MantlePlumeThermalUpwellingNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MantlePlumeThermalUpwellingNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
