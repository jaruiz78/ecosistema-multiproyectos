package com.corp.proyectoecotasasoberanatax.application.service;

import com.corp.proyectoecotasasoberanatax.domain.model.EcotasaSoberanaTax;
import com.corp.proyectoecotasasoberanatax.domain.port.in.ManageEcotasaSoberanaTaxUseCase;
import com.corp.proyectoecotasasoberanatax.domain.port.out.EcotasaSoberanaTaxRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class EcotasaSoberanaTaxApplicationService implements ManageEcotasaSoberanaTaxUseCase {

    private final EcotasaSoberanaTaxRepositoryPort repositoryPort;

    public EcotasaSoberanaTaxApplicationService(EcotasaSoberanaTaxRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EcotasaSoberanaTax createEcotasaSoberanaTax(String tenantId, String title, double value) {
        EcotasaSoberanaTax entity = new EcotasaSoberanaTax(
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
    public Optional<EcotasaSoberanaTax> findEcotasaSoberanaTaxById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EcotasaSoberanaTax processOptimization(String id, String tenantId) {
        EcotasaSoberanaTax existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EcotasaSoberanaTax optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
