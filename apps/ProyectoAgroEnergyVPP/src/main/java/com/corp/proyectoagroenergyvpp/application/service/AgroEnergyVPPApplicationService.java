package com.corp.proyectoagroenergyvpp.application.service;

import com.corp.proyectoagroenergyvpp.domain.model.AgroEnergyVPP;
import com.corp.proyectoagroenergyvpp.domain.port.in.ManageAgroEnergyVPPUseCase;
import com.corp.proyectoagroenergyvpp.domain.port.out.AgroEnergyVPPRepositoryPort;
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
public class AgroEnergyVPPApplicationService implements ManageAgroEnergyVPPUseCase {

    private final AgroEnergyVPPRepositoryPort repositoryPort;

    public AgroEnergyVPPApplicationService(AgroEnergyVPPRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AgroEnergyVPP createAgroEnergyVPP(String tenantId, String title, double value) {
        AgroEnergyVPP entity = new AgroEnergyVPP(
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
    public Optional<AgroEnergyVPP> findAgroEnergyVPPById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AgroEnergyVPP processOptimization(String id, String tenantId) {
        AgroEnergyVPP existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AgroEnergyVPP optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
