package com.corp.proyectodefensa.application.service;

import com.corp.proyectodefensa.domain.model.Defensa;
import com.corp.proyectodefensa.domain.port.in.ManageDefensaUseCase;
import com.corp.proyectodefensa.domain.port.out.DefensaRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_10_identidad_soberana_privacidad_zkp">FACULTAD_XI: Identidad Soberana & Zero-Trust BeyondCorp</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class DefensaApplicationService implements ManageDefensaUseCase {

    private final DefensaRepositoryPort repositoryPort;

    public DefensaApplicationService(DefensaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Defensa createDefensa(String tenantId, String title, double value) {
        Defensa entity = new Defensa(
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
    public Optional<Defensa> findDefensaById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Defensa processOptimization(String id, String tenantId) {
        Defensa existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Defensa optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
