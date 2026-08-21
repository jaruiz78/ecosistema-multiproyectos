package com.corp.proyectocrosschainassetsettlement.application.service;

import com.corp.proyectocrosschainassetsettlement.domain.model.HtlcAtomicSwapEscrowLockToken;
import com.corp.proyectocrosschainassetsettlement.domain.port.in.ManageHtlcAtomicSwapEscrowLockTokenUseCase;
import com.corp.proyectocrosschainassetsettlement.domain.port.out.HtlcAtomicSwapEscrowLockTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HtlcAtomicSwapEscrowLockToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HtlcAtomicSwapEscrowLockTokenApplicationService implements ManageHtlcAtomicSwapEscrowLockTokenUseCase {

    private final HtlcAtomicSwapEscrowLockTokenRepositoryPort repositoryPort;

    public HtlcAtomicSwapEscrowLockTokenApplicationService(HtlcAtomicSwapEscrowLockTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HtlcAtomicSwapEscrowLockToken createHtlcAtomicSwapEscrowLockToken(String tenantId, String title, double value) {
        HtlcAtomicSwapEscrowLockToken entity = new HtlcAtomicSwapEscrowLockToken(
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
    public Optional<HtlcAtomicSwapEscrowLockToken> findHtlcAtomicSwapEscrowLockTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HtlcAtomicSwapEscrowLockToken processOptimization(String id, String tenantId) {
        HtlcAtomicSwapEscrowLockToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HtlcAtomicSwapEscrowLockToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
