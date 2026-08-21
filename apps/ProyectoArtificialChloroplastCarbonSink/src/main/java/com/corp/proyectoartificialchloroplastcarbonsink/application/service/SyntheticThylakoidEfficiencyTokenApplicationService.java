package com.corp.proyectoartificialchloroplastcarbonsink.application.service;

import com.corp.proyectoartificialchloroplastcarbonsink.domain.model.SyntheticThylakoidEfficiencyToken;
import com.corp.proyectoartificialchloroplastcarbonsink.domain.port.in.ManageSyntheticThylakoidEfficiencyTokenUseCase;
import com.corp.proyectoartificialchloroplastcarbonsink.domain.port.out.SyntheticThylakoidEfficiencyTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SyntheticThylakoidEfficiencyToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SyntheticThylakoidEfficiencyTokenApplicationService implements ManageSyntheticThylakoidEfficiencyTokenUseCase {

    private final SyntheticThylakoidEfficiencyTokenRepositoryPort repositoryPort;

    public SyntheticThylakoidEfficiencyTokenApplicationService(SyntheticThylakoidEfficiencyTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SyntheticThylakoidEfficiencyToken createSyntheticThylakoidEfficiencyToken(String tenantId, String title, double value) {
        SyntheticThylakoidEfficiencyToken entity = new SyntheticThylakoidEfficiencyToken(
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
    public Optional<SyntheticThylakoidEfficiencyToken> findSyntheticThylakoidEfficiencyTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SyntheticThylakoidEfficiencyToken processOptimization(String id, String tenantId) {
        SyntheticThylakoidEfficiencyToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SyntheticThylakoidEfficiencyToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
