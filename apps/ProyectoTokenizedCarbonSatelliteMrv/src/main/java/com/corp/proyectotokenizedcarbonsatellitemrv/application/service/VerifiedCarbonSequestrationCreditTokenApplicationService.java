package com.corp.proyectotokenizedcarbonsatellitemrv.application.service;

import com.corp.proyectotokenizedcarbonsatellitemrv.domain.model.VerifiedCarbonSequestrationCreditToken;
import com.corp.proyectotokenizedcarbonsatellitemrv.domain.port.in.ManageVerifiedCarbonSequestrationCreditTokenUseCase;
import com.corp.proyectotokenizedcarbonsatellitemrv.domain.port.out.VerifiedCarbonSequestrationCreditTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de VerifiedCarbonSequestrationCreditToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class VerifiedCarbonSequestrationCreditTokenApplicationService implements ManageVerifiedCarbonSequestrationCreditTokenUseCase {

    private final VerifiedCarbonSequestrationCreditTokenRepositoryPort repositoryPort;

    public VerifiedCarbonSequestrationCreditTokenApplicationService(VerifiedCarbonSequestrationCreditTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public VerifiedCarbonSequestrationCreditToken createVerifiedCarbonSequestrationCreditToken(String tenantId, String title, double value) {
        VerifiedCarbonSequestrationCreditToken entity = new VerifiedCarbonSequestrationCreditToken(
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
    public Optional<VerifiedCarbonSequestrationCreditToken> findVerifiedCarbonSequestrationCreditTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public VerifiedCarbonSequestrationCreditToken processOptimization(String id, String tenantId) {
        VerifiedCarbonSequestrationCreditToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        VerifiedCarbonSequestrationCreditToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
