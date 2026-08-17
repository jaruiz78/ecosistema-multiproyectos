package com.corp.proyectosmartagrisupplychain.application.service;

import com.corp.proyectosmartagrisupplychain.domain.model.SmartAgriSupplyChain;
import com.corp.proyectosmartagrisupplychain.domain.port.in.ManageSmartAgriSupplyChainUseCase;
import com.corp.proyectosmartagrisupplychain.domain.port.out.SmartAgriSupplyChainRepositoryPort;
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
public class SmartAgriSupplyChainApplicationService implements ManageSmartAgriSupplyChainUseCase {

    private final SmartAgriSupplyChainRepositoryPort repositoryPort;

    public SmartAgriSupplyChainApplicationService(SmartAgriSupplyChainRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SmartAgriSupplyChain createSmartAgriSupplyChain(String tenantId, String title, double value) {
        SmartAgriSupplyChain entity = new SmartAgriSupplyChain(
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
    public Optional<SmartAgriSupplyChain> findSmartAgriSupplyChainById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SmartAgriSupplyChain processOptimization(String id, String tenantId) {
        SmartAgriSupplyChain existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SmartAgriSupplyChain optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
