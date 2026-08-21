package com.corp.proyectolastmiledronedeliverygrid.application.service;

import com.corp.proyectolastmiledronedeliverygrid.domain.model.DroneDeliveryAirspaceVolumeToken;
import com.corp.proyectolastmiledronedeliverygrid.domain.port.in.ManageDroneDeliveryAirspaceVolumeTokenUseCase;
import com.corp.proyectolastmiledronedeliverygrid.domain.port.out.DroneDeliveryAirspaceVolumeTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DroneDeliveryAirspaceVolumeToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DroneDeliveryAirspaceVolumeTokenApplicationService implements ManageDroneDeliveryAirspaceVolumeTokenUseCase {

    private final DroneDeliveryAirspaceVolumeTokenRepositoryPort repositoryPort;

    public DroneDeliveryAirspaceVolumeTokenApplicationService(DroneDeliveryAirspaceVolumeTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DroneDeliveryAirspaceVolumeToken createDroneDeliveryAirspaceVolumeToken(String tenantId, String title, double value) {
        DroneDeliveryAirspaceVolumeToken entity = new DroneDeliveryAirspaceVolumeToken(
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
    public Optional<DroneDeliveryAirspaceVolumeToken> findDroneDeliveryAirspaceVolumeTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DroneDeliveryAirspaceVolumeToken processOptimization(String id, String tenantId) {
        DroneDeliveryAirspaceVolumeToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DroneDeliveryAirspaceVolumeToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
