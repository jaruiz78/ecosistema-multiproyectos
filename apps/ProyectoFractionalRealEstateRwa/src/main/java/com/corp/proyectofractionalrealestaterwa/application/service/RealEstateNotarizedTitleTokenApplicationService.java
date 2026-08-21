package com.corp.proyectofractionalrealestaterwa.application.service;

import com.corp.proyectofractionalrealestaterwa.domain.model.RealEstateNotarizedTitleToken;
import com.corp.proyectofractionalrealestaterwa.domain.port.in.ManageRealEstateNotarizedTitleTokenUseCase;
import com.corp.proyectofractionalrealestaterwa.domain.port.out.RealEstateNotarizedTitleTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de RealEstateNotarizedTitleToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class RealEstateNotarizedTitleTokenApplicationService implements ManageRealEstateNotarizedTitleTokenUseCase {

    private final RealEstateNotarizedTitleTokenRepositoryPort repositoryPort;

    public RealEstateNotarizedTitleTokenApplicationService(RealEstateNotarizedTitleTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public RealEstateNotarizedTitleToken createRealEstateNotarizedTitleToken(String tenantId, String title, double value) {
        RealEstateNotarizedTitleToken entity = new RealEstateNotarizedTitleToken(
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
    public Optional<RealEstateNotarizedTitleToken> findRealEstateNotarizedTitleTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public RealEstateNotarizedTitleToken processOptimization(String id, String tenantId) {
        RealEstateNotarizedTitleToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        RealEstateNotarizedTitleToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
