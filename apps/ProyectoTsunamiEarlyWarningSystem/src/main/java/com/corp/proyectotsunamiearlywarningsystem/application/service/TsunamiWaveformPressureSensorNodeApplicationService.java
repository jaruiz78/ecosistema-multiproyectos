package com.corp.proyectotsunamiearlywarningsystem.application.service;

import com.corp.proyectotsunamiearlywarningsystem.domain.model.TsunamiWaveformPressureSensorNode;
import com.corp.proyectotsunamiearlywarningsystem.domain.port.in.ManageTsunamiWaveformPressureSensorNodeUseCase;
import com.corp.proyectotsunamiearlywarningsystem.domain.port.out.TsunamiWaveformPressureSensorNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de TsunamiWaveformPressureSensorNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class TsunamiWaveformPressureSensorNodeApplicationService implements ManageTsunamiWaveformPressureSensorNodeUseCase {

    private final TsunamiWaveformPressureSensorNodeRepositoryPort repositoryPort;

    public TsunamiWaveformPressureSensorNodeApplicationService(TsunamiWaveformPressureSensorNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public TsunamiWaveformPressureSensorNode createTsunamiWaveformPressureSensorNode(String tenantId, String title, double value) {
        TsunamiWaveformPressureSensorNode entity = new TsunamiWaveformPressureSensorNode(
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
    public Optional<TsunamiWaveformPressureSensorNode> findTsunamiWaveformPressureSensorNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public TsunamiWaveformPressureSensorNode processOptimization(String id, String tenantId) {
        TsunamiWaveformPressureSensorNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        TsunamiWaveformPressureSensorNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
