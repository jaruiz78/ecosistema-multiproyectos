package com.corp.proyectoairporttouristintermodal.application.service;

import com.corp.proyectoairporttouristintermodal.domain.model.AirportTouristIntermodal;
import com.corp.proyectoairporttouristintermodal.domain.port.in.ManageAirportTouristIntermodalUseCase;
import com.corp.proyectoairporttouristintermodal.domain.port.out.AirportTouristIntermodalRepositoryPort;
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
public class AirportTouristIntermodalApplicationService implements ManageAirportTouristIntermodalUseCase {

    private final AirportTouristIntermodalRepositoryPort repositoryPort;

    public AirportTouristIntermodalApplicationService(AirportTouristIntermodalRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AirportTouristIntermodal createAirportTouristIntermodal(String tenantId, String title, double value) {
        AirportTouristIntermodal entity = new AirportTouristIntermodal(
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
    public Optional<AirportTouristIntermodal> findAirportTouristIntermodalById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AirportTouristIntermodal processOptimization(String id, String tenantId) {
        AirportTouristIntermodal existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AirportTouristIntermodal optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
