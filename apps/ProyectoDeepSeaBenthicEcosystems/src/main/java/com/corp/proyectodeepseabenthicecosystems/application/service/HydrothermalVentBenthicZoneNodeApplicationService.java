package com.corp.proyectodeepseabenthicecosystems.application.service;

import com.corp.proyectodeepseabenthicecosystems.domain.model.HydrothermalVentBenthicZoneNode;
import com.corp.proyectodeepseabenthicecosystems.domain.port.in.ManageHydrothermalVentBenthicZoneNodeUseCase;
import com.corp.proyectodeepseabenthicecosystems.domain.port.out.HydrothermalVentBenthicZoneNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HydrothermalVentBenthicZoneNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HydrothermalVentBenthicZoneNodeApplicationService implements ManageHydrothermalVentBenthicZoneNodeUseCase {

    private final HydrothermalVentBenthicZoneNodeRepositoryPort repositoryPort;

    public HydrothermalVentBenthicZoneNodeApplicationService(HydrothermalVentBenthicZoneNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HydrothermalVentBenthicZoneNode createHydrothermalVentBenthicZoneNode(String tenantId, String title, double value) {
        HydrothermalVentBenthicZoneNode entity = new HydrothermalVentBenthicZoneNode(
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
    public Optional<HydrothermalVentBenthicZoneNode> findHydrothermalVentBenthicZoneNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HydrothermalVentBenthicZoneNode processOptimization(String id, String tenantId) {
        HydrothermalVentBenthicZoneNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HydrothermalVentBenthicZoneNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
