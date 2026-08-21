package com.corp.proyectoallostericdrugdiscovery.application.service;

import com.corp.proyectoallostericdrugdiscovery.domain.model.CrypticBindingPocketVolumeToken;
import com.corp.proyectoallostericdrugdiscovery.domain.port.in.ManageCrypticBindingPocketVolumeTokenUseCase;
import com.corp.proyectoallostericdrugdiscovery.domain.port.out.CrypticBindingPocketVolumeTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CrypticBindingPocketVolumeToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CrypticBindingPocketVolumeTokenApplicationService implements ManageCrypticBindingPocketVolumeTokenUseCase {

    private final CrypticBindingPocketVolumeTokenRepositoryPort repositoryPort;

    public CrypticBindingPocketVolumeTokenApplicationService(CrypticBindingPocketVolumeTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CrypticBindingPocketVolumeToken createCrypticBindingPocketVolumeToken(String tenantId, String title, double value) {
        CrypticBindingPocketVolumeToken entity = new CrypticBindingPocketVolumeToken(
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
    public Optional<CrypticBindingPocketVolumeToken> findCrypticBindingPocketVolumeTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CrypticBindingPocketVolumeToken processOptimization(String id, String tenantId) {
        CrypticBindingPocketVolumeToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CrypticBindingPocketVolumeToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
