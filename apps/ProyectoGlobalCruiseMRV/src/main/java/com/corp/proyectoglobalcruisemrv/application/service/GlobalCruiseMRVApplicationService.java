package com.corp.proyectoglobalcruisemrv.application.service;

import com.corp.proyectoglobalcruisemrv.domain.model.GlobalCruiseMRV;
import com.corp.proyectoglobalcruisemrv.domain.port.in.ManageGlobalCruiseMRVUseCase;
import com.corp.proyectoglobalcruisemrv.domain.port.out.GlobalCruiseMRVRepositoryPort;
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
public class GlobalCruiseMRVApplicationService implements ManageGlobalCruiseMRVUseCase {

    private final GlobalCruiseMRVRepositoryPort repositoryPort;

    public GlobalCruiseMRVApplicationService(GlobalCruiseMRVRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GlobalCruiseMRV createGlobalCruiseMRV(String tenantId, String title, double value) {
        GlobalCruiseMRV entity = new GlobalCruiseMRV(
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
    public Optional<GlobalCruiseMRV> findGlobalCruiseMRVById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GlobalCruiseMRV processOptimization(String id, String tenantId) {
        GlobalCruiseMRV existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GlobalCruiseMRV optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
