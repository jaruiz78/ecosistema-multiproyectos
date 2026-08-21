package com.corp.proyectoparametricweatherinsurance.application.service;

import com.corp.proyectoparametricweatherinsurance.domain.model.ParametricInsurancePolicyPayoutToken;
import com.corp.proyectoparametricweatherinsurance.domain.port.in.ManageParametricInsurancePolicyPayoutTokenUseCase;
import com.corp.proyectoparametricweatherinsurance.domain.port.out.ParametricInsurancePolicyPayoutTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ParametricInsurancePolicyPayoutToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ParametricInsurancePolicyPayoutTokenApplicationService implements ManageParametricInsurancePolicyPayoutTokenUseCase {

    private final ParametricInsurancePolicyPayoutTokenRepositoryPort repositoryPort;

    public ParametricInsurancePolicyPayoutTokenApplicationService(ParametricInsurancePolicyPayoutTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ParametricInsurancePolicyPayoutToken createParametricInsurancePolicyPayoutToken(String tenantId, String title, double value) {
        ParametricInsurancePolicyPayoutToken entity = new ParametricInsurancePolicyPayoutToken(
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
    public Optional<ParametricInsurancePolicyPayoutToken> findParametricInsurancePolicyPayoutTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ParametricInsurancePolicyPayoutToken processOptimization(String id, String tenantId) {
        ParametricInsurancePolicyPayoutToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ParametricInsurancePolicyPayoutToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
