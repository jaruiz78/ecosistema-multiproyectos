package com.corp.proyectoaavvectortherapeuticdesign.application.service;

import com.corp.proyectoaavvectortherapeuticdesign.domain.model.AavCapsidTropismVectorToken;
import com.corp.proyectoaavvectortherapeuticdesign.domain.port.in.ManageAavCapsidTropismVectorTokenUseCase;
import com.corp.proyectoaavvectortherapeuticdesign.domain.port.out.AavCapsidTropismVectorTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AavCapsidTropismVectorToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AavCapsidTropismVectorTokenApplicationService implements ManageAavCapsidTropismVectorTokenUseCase {

    private final AavCapsidTropismVectorTokenRepositoryPort repositoryPort;

    public AavCapsidTropismVectorTokenApplicationService(AavCapsidTropismVectorTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AavCapsidTropismVectorToken createAavCapsidTropismVectorToken(String tenantId, String title, double value) {
        AavCapsidTropismVectorToken entity = new AavCapsidTropismVectorToken(
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
    public Optional<AavCapsidTropismVectorToken> findAavCapsidTropismVectorTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AavCapsidTropismVectorToken processOptimization(String id, String tenantId) {
        AavCapsidTropismVectorToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AavCapsidTropismVectorToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
