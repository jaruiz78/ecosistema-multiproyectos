package com.corp.proyectopharmacoldchain.application.service;

import com.corp.proyectopharmacoldchain.domain.model.PharmaColdChain;
import com.corp.proyectopharmacoldchain.domain.port.in.ManagePharmaColdChainUseCase;
import com.corp.proyectopharmacoldchain.domain.port.out.PharmaColdChainRepositoryPort;
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
public class PharmaColdChainApplicationService implements ManagePharmaColdChainUseCase {

    private final PharmaColdChainRepositoryPort repositoryPort;

    public PharmaColdChainApplicationService(PharmaColdChainRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PharmaColdChain createPharmaColdChain(String tenantId, String title, double value) {
        PharmaColdChain entity = new PharmaColdChain(
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
    public Optional<PharmaColdChain> findPharmaColdChainById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PharmaColdChain processOptimization(String id, String tenantId) {
        PharmaColdChain existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PharmaColdChain optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
