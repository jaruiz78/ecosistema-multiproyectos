package com.corp.proyectoenergia.application.service;

import com.corp.proyectoenergia.domain.model.Energia;
import com.corp.proyectoenergia.domain.port.in.ManageEnergiaUseCase;
import com.corp.proyectoenergia.domain.port.out.EnergiaRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion">FACULTAD_V: Gemelo Digital PEPS, EnKF & Física</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class EnergiaApplicationService implements ManageEnergiaUseCase {

    private final EnergiaRepositoryPort repositoryPort;

    public EnergiaApplicationService(EnergiaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Energia createEnergia(String tenantId, String title, double value) {
        Energia entity = new Energia(
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
    public Optional<Energia> findEnergiaById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Energia processOptimization(String id, String tenantId) {
        Energia existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Energia optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
