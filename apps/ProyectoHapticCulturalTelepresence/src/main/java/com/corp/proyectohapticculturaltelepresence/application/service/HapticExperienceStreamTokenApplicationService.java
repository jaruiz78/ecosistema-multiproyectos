package com.corp.proyectohapticculturaltelepresence.application.service;

import com.corp.proyectohapticculturaltelepresence.domain.model.HapticExperienceStreamToken;
import com.corp.proyectohapticculturaltelepresence.domain.port.in.ManageHapticExperienceStreamTokenUseCase;
import com.corp.proyectohapticculturaltelepresence.domain.port.out.HapticExperienceStreamTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HapticExperienceStreamToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HapticExperienceStreamTokenApplicationService implements ManageHapticExperienceStreamTokenUseCase {

    private final HapticExperienceStreamTokenRepositoryPort repositoryPort;

    public HapticExperienceStreamTokenApplicationService(HapticExperienceStreamTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HapticExperienceStreamToken createHapticExperienceStreamToken(String tenantId, String title, double value) {
        HapticExperienceStreamToken entity = new HapticExperienceStreamToken(
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
    public Optional<HapticExperienceStreamToken> findHapticExperienceStreamTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HapticExperienceStreamToken processOptimization(String id, String tenantId) {
        HapticExperienceStreamToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HapticExperienceStreamToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
