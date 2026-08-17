package com.corp.proyectosmartdestinationdti.application.service;

import com.corp.proyectosmartdestinationdti.domain.model.SmartDestinationDTI;
import com.corp.proyectosmartdestinationdti.domain.port.in.ManageSmartDestinationDTIUseCase;
import com.corp.proyectosmartdestinationdti.domain.port.out.SmartDestinationDTIRepositoryPort;
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
public class SmartDestinationDTIApplicationService implements ManageSmartDestinationDTIUseCase {

    private final SmartDestinationDTIRepositoryPort repositoryPort;

    public SmartDestinationDTIApplicationService(SmartDestinationDTIRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SmartDestinationDTI createSmartDestinationDTI(String tenantId, String title, double value) {
        SmartDestinationDTI entity = new SmartDestinationDTI(
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
    public Optional<SmartDestinationDTI> findSmartDestinationDTIById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SmartDestinationDTI processOptimization(String id, String tenantId) {
        SmartDestinationDTI existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SmartDestinationDTI optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
