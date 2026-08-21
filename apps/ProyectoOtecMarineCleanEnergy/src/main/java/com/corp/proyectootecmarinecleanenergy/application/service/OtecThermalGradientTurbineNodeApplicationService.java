package com.corp.proyectootecmarinecleanenergy.application.service;

import com.corp.proyectootecmarinecleanenergy.domain.model.OtecThermalGradientTurbineNode;
import com.corp.proyectootecmarinecleanenergy.domain.port.in.ManageOtecThermalGradientTurbineNodeUseCase;
import com.corp.proyectootecmarinecleanenergy.domain.port.out.OtecThermalGradientTurbineNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de OtecThermalGradientTurbineNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class OtecThermalGradientTurbineNodeApplicationService implements ManageOtecThermalGradientTurbineNodeUseCase {

    private final OtecThermalGradientTurbineNodeRepositoryPort repositoryPort;

    public OtecThermalGradientTurbineNodeApplicationService(OtecThermalGradientTurbineNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public OtecThermalGradientTurbineNode createOtecThermalGradientTurbineNode(String tenantId, String title, double value) {
        OtecThermalGradientTurbineNode entity = new OtecThermalGradientTurbineNode(
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
    public Optional<OtecThermalGradientTurbineNode> findOtecThermalGradientTurbineNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public OtecThermalGradientTurbineNode processOptimization(String id, String tenantId) {
        OtecThermalGradientTurbineNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        OtecThermalGradientTurbineNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
