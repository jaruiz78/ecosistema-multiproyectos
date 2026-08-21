package com.corp.proyectoalgalbiodieselrefinery.application.service;

import com.corp.proyectoalgalbiodieselrefinery.domain.model.LipidToBiodieselYieldConversionToken;
import com.corp.proyectoalgalbiodieselrefinery.domain.port.in.ManageLipidToBiodieselYieldConversionTokenUseCase;
import com.corp.proyectoalgalbiodieselrefinery.domain.port.out.LipidToBiodieselYieldConversionTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de LipidToBiodieselYieldConversionToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class LipidToBiodieselYieldConversionTokenApplicationService implements ManageLipidToBiodieselYieldConversionTokenUseCase {

    private final LipidToBiodieselYieldConversionTokenRepositoryPort repositoryPort;

    public LipidToBiodieselYieldConversionTokenApplicationService(LipidToBiodieselYieldConversionTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public LipidToBiodieselYieldConversionToken createLipidToBiodieselYieldConversionToken(String tenantId, String title, double value) {
        LipidToBiodieselYieldConversionToken entity = new LipidToBiodieselYieldConversionToken(
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
    public Optional<LipidToBiodieselYieldConversionToken> findLipidToBiodieselYieldConversionTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public LipidToBiodieselYieldConversionToken processOptimization(String id, String tenantId) {
        LipidToBiodieselYieldConversionToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        LipidToBiodieselYieldConversionToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
