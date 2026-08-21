package com.corp.proyectoquantumsecurebanking.application.service;

import com.corp.proyectoquantumsecurebanking.domain.model.QuantumVaultAccount;
import com.corp.proyectoquantumsecurebanking.domain.port.in.ManageQuantumVaultAccountUseCase;
import com.corp.proyectoquantumsecurebanking.domain.port.out.QuantumVaultAccountRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de QuantumVaultAccount.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class QuantumVaultAccountApplicationService implements ManageQuantumVaultAccountUseCase {

    private final QuantumVaultAccountRepositoryPort repositoryPort;

    public QuantumVaultAccountApplicationService(QuantumVaultAccountRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QuantumVaultAccount createQuantumVaultAccount(String tenantId, String title, double value) {
        QuantumVaultAccount entity = new QuantumVaultAccount(
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
    public Optional<QuantumVaultAccount> findQuantumVaultAccountById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QuantumVaultAccount processOptimization(String id, String tenantId) {
        QuantumVaultAccount existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QuantumVaultAccount optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
