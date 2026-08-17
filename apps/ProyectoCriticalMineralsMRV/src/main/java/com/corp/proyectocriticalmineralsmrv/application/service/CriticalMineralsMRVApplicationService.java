package com.corp.proyectocriticalmineralsmrv.application.service;

import com.corp.proyectocriticalmineralsmrv.domain.model.CriticalMineralsMRV;
import com.corp.proyectocriticalmineralsmrv.domain.port.in.ManageCriticalMineralsMRVUseCase;
import com.corp.proyectocriticalmineralsmrv.domain.port.out.CriticalMineralsMRVRepositoryPort;
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
public class CriticalMineralsMRVApplicationService implements ManageCriticalMineralsMRVUseCase {

    private final CriticalMineralsMRVRepositoryPort repositoryPort;

    public CriticalMineralsMRVApplicationService(CriticalMineralsMRVRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CriticalMineralsMRV createCriticalMineralsMRV(String tenantId, String title, double value) {
        CriticalMineralsMRV entity = new CriticalMineralsMRV(
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
    public Optional<CriticalMineralsMRV> findCriticalMineralsMRVById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CriticalMineralsMRV processOptimization(String id, String tenantId) {
        CriticalMineralsMRV existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CriticalMineralsMRV optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
