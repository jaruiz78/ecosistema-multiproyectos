package com.corp.proyectoquantumkeyescrowsharding.application.service;

import com.corp.proyectoquantumkeyescrowsharding.domain.model.ShamirPqcKeyShardBundleToken;
import com.corp.proyectoquantumkeyescrowsharding.domain.port.in.ManageShamirPqcKeyShardBundleTokenUseCase;
import com.corp.proyectoquantumkeyescrowsharding.domain.port.out.ShamirPqcKeyShardBundleTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ShamirPqcKeyShardBundleToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ShamirPqcKeyShardBundleTokenApplicationService implements ManageShamirPqcKeyShardBundleTokenUseCase {

    private final ShamirPqcKeyShardBundleTokenRepositoryPort repositoryPort;

    public ShamirPqcKeyShardBundleTokenApplicationService(ShamirPqcKeyShardBundleTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ShamirPqcKeyShardBundleToken createShamirPqcKeyShardBundleToken(String tenantId, String title, double value) {
        ShamirPqcKeyShardBundleToken entity = new ShamirPqcKeyShardBundleToken(
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
    public Optional<ShamirPqcKeyShardBundleToken> findShamirPqcKeyShardBundleTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ShamirPqcKeyShardBundleToken processOptimization(String id, String tenantId) {
        ShamirPqcKeyShardBundleToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ShamirPqcKeyShardBundleToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
