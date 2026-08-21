package com.corp.proyectoorthogonalribosomepolymers.application.service;

import com.corp.proyectoorthogonalribosomepolymers.domain.model.UnnaturalAminoAcidIncorporationToken;
import com.corp.proyectoorthogonalribosomepolymers.domain.port.in.ManageUnnaturalAminoAcidIncorporationTokenUseCase;
import com.corp.proyectoorthogonalribosomepolymers.domain.port.out.UnnaturalAminoAcidIncorporationTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de UnnaturalAminoAcidIncorporationToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class UnnaturalAminoAcidIncorporationTokenApplicationService implements ManageUnnaturalAminoAcidIncorporationTokenUseCase {

    private final UnnaturalAminoAcidIncorporationTokenRepositoryPort repositoryPort;

    public UnnaturalAminoAcidIncorporationTokenApplicationService(UnnaturalAminoAcidIncorporationTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public UnnaturalAminoAcidIncorporationToken createUnnaturalAminoAcidIncorporationToken(String tenantId, String title, double value) {
        UnnaturalAminoAcidIncorporationToken entity = new UnnaturalAminoAcidIncorporationToken(
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
    public Optional<UnnaturalAminoAcidIncorporationToken> findUnnaturalAminoAcidIncorporationTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public UnnaturalAminoAcidIncorporationToken processOptimization(String id, String tenantId) {
        UnnaturalAminoAcidIncorporationToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        UnnaturalAminoAcidIncorporationToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
