package com.corp.proyectospintronicpersistentmemory.application.service;

import com.corp.proyectospintronicpersistentmemory.domain.model.SpinTorqueTransferCacheToken;
import com.corp.proyectospintronicpersistentmemory.domain.port.in.ManageSpinTorqueTransferCacheTokenUseCase;
import com.corp.proyectospintronicpersistentmemory.domain.port.out.SpinTorqueTransferCacheTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SpinTorqueTransferCacheToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SpinTorqueTransferCacheTokenApplicationService implements ManageSpinTorqueTransferCacheTokenUseCase {

    private final SpinTorqueTransferCacheTokenRepositoryPort repositoryPort;

    public SpinTorqueTransferCacheTokenApplicationService(SpinTorqueTransferCacheTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SpinTorqueTransferCacheToken createSpinTorqueTransferCacheToken(String tenantId, String title, double value) {
        SpinTorqueTransferCacheToken entity = new SpinTorqueTransferCacheToken(
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
    public Optional<SpinTorqueTransferCacheToken> findSpinTorqueTransferCacheTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SpinTorqueTransferCacheToken processOptimization(String id, String tenantId) {
        SpinTorqueTransferCacheToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SpinTorqueTransferCacheToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
