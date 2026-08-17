package com.corp.proyectovpp.application.service;

import com.corp.proyectovpp.domain.model.VPP;
import com.corp.proyectovpp.domain.port.in.ManageVPPUseCase;
import com.corp.proyectovpp.domain.port.out.VPPRepositoryPort;
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
public class VPPApplicationService implements ManageVPPUseCase {

    private final VPPRepositoryPort repositoryPort;

    public VPPApplicationService(VPPRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public VPP createVPP(String tenantId, String title, double value) {
        VPP entity = new VPP(
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
    public Optional<VPP> findVPPById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public VPP processOptimization(String id, String tenantId) {
        VPP existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        VPP optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
