package com.corp.proyectoairlineinterlinebaggage.application.service;

import com.corp.proyectoairlineinterlinebaggage.domain.model.AirlineInterlineBaggage;
import com.corp.proyectoairlineinterlinebaggage.domain.port.in.ManageAirlineInterlineBaggageUseCase;
import com.corp.proyectoairlineinterlinebaggage.domain.port.out.AirlineInterlineBaggageRepositoryPort;
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
public class AirlineInterlineBaggageApplicationService implements ManageAirlineInterlineBaggageUseCase {

    private final AirlineInterlineBaggageRepositoryPort repositoryPort;

    public AirlineInterlineBaggageApplicationService(AirlineInterlineBaggageRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AirlineInterlineBaggage createAirlineInterlineBaggage(String tenantId, String title, double value) {
        AirlineInterlineBaggage entity = new AirlineInterlineBaggage(
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
    public Optional<AirlineInterlineBaggage> findAirlineInterlineBaggageById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AirlineInterlineBaggage processOptimization(String id, String tenantId) {
        AirlineInterlineBaggage existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AirlineInterlineBaggage optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
