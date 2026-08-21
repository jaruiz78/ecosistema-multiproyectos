package com.corp.proyectoquantumtimedistributionnetwork.application.service;

import com.corp.proyectoquantumtimedistributionnetwork.domain.model.QuantumPicosecondClockSyncToken;
import com.corp.proyectoquantumtimedistributionnetwork.domain.port.in.ManageQuantumPicosecondClockSyncTokenUseCase;
import com.corp.proyectoquantumtimedistributionnetwork.domain.port.out.QuantumPicosecondClockSyncTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de QuantumPicosecondClockSyncToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class QuantumPicosecondClockSyncTokenApplicationService implements ManageQuantumPicosecondClockSyncTokenUseCase {

    private final QuantumPicosecondClockSyncTokenRepositoryPort repositoryPort;

    public QuantumPicosecondClockSyncTokenApplicationService(QuantumPicosecondClockSyncTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QuantumPicosecondClockSyncToken createQuantumPicosecondClockSyncToken(String tenantId, String title, double value) {
        QuantumPicosecondClockSyncToken entity = new QuantumPicosecondClockSyncToken(
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
    public Optional<QuantumPicosecondClockSyncToken> findQuantumPicosecondClockSyncTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QuantumPicosecondClockSyncToken processOptimization(String id, String tenantId) {
        QuantumPicosecondClockSyncToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QuantumPicosecondClockSyncToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
