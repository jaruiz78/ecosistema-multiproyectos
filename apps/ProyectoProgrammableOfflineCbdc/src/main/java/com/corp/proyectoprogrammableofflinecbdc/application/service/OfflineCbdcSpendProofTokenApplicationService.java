package com.corp.proyectoprogrammableofflinecbdc.application.service;

import com.corp.proyectoprogrammableofflinecbdc.domain.model.OfflineCbdcSpendProofToken;
import com.corp.proyectoprogrammableofflinecbdc.domain.port.in.ManageOfflineCbdcSpendProofTokenUseCase;
import com.corp.proyectoprogrammableofflinecbdc.domain.port.out.OfflineCbdcSpendProofTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de OfflineCbdcSpendProofToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class OfflineCbdcSpendProofTokenApplicationService implements ManageOfflineCbdcSpendProofTokenUseCase {

    private final OfflineCbdcSpendProofTokenRepositoryPort repositoryPort;

    public OfflineCbdcSpendProofTokenApplicationService(OfflineCbdcSpendProofTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public OfflineCbdcSpendProofToken createOfflineCbdcSpendProofToken(String tenantId, String title, double value) {
        OfflineCbdcSpendProofToken entity = new OfflineCbdcSpendProofToken(
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
    public Optional<OfflineCbdcSpendProofToken> findOfflineCbdcSpendProofTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public OfflineCbdcSpendProofToken processOptimization(String id, String tenantId) {
        OfflineCbdcSpendProofToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        OfflineCbdcSpendProofToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
