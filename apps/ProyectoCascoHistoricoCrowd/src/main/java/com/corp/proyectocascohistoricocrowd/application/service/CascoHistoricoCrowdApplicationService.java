package com.corp.proyectocascohistoricocrowd.application.service;

import com.corp.proyectocascohistoricocrowd.domain.model.CascoHistoricoCrowd;
import com.corp.proyectocascohistoricocrowd.domain.port.in.ManageCascoHistoricoCrowdUseCase;
import com.corp.proyectocascohistoricocrowd.domain.port.out.CascoHistoricoCrowdRepositoryPort;
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
public class CascoHistoricoCrowdApplicationService implements ManageCascoHistoricoCrowdUseCase {

    private final CascoHistoricoCrowdRepositoryPort repositoryPort;

    public CascoHistoricoCrowdApplicationService(CascoHistoricoCrowdRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CascoHistoricoCrowd createCascoHistoricoCrowd(String tenantId, String title, double value) {
        CascoHistoricoCrowd entity = new CascoHistoricoCrowd(
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
    public Optional<CascoHistoricoCrowd> findCascoHistoricoCrowdById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CascoHistoricoCrowd processOptimization(String id, String tenantId) {
        CascoHistoricoCrowd existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CascoHistoricoCrowd optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
