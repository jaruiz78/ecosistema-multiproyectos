package com.corp.proyectocrisprbaseeditingtherapy.application.service;

import com.corp.proyectocrisprbaseeditingtherapy.domain.model.BaseEditorTransitionEfficiencyToken;
import com.corp.proyectocrisprbaseeditingtherapy.domain.port.in.ManageBaseEditorTransitionEfficiencyTokenUseCase;
import com.corp.proyectocrisprbaseeditingtherapy.domain.port.out.BaseEditorTransitionEfficiencyTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BaseEditorTransitionEfficiencyToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class BaseEditorTransitionEfficiencyTokenApplicationService implements ManageBaseEditorTransitionEfficiencyTokenUseCase {

    private final BaseEditorTransitionEfficiencyTokenRepositoryPort repositoryPort;

    public BaseEditorTransitionEfficiencyTokenApplicationService(BaseEditorTransitionEfficiencyTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BaseEditorTransitionEfficiencyToken createBaseEditorTransitionEfficiencyToken(String tenantId, String title, double value) {
        BaseEditorTransitionEfficiencyToken entity = new BaseEditorTransitionEfficiencyToken(
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
    public Optional<BaseEditorTransitionEfficiencyToken> findBaseEditorTransitionEfficiencyTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BaseEditorTransitionEfficiencyToken processOptimization(String id, String tenantId) {
        BaseEditorTransitionEfficiencyToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BaseEditorTransitionEfficiencyToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
