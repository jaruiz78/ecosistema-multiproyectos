package com.corp.proyectocarbonledger.application.service;

import com.corp.proyectocarbonledger.domain.model.CarbonLedger;
import com.corp.proyectocarbonledger.domain.port.in.ManageCarbonLedgerUseCase;
import com.corp.proyectocarbonledger.domain.port.out.CarbonLedgerRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_sistemas_distribuidos">FACULTAD_II: Sistemas Distribuidos, Consenso & TLA+</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class CarbonLedgerApplicationService implements ManageCarbonLedgerUseCase {

    private final CarbonLedgerRepositoryPort repositoryPort;

    public CarbonLedgerApplicationService(CarbonLedgerRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CarbonLedger createCarbonLedger(String tenantId, String title, double value) {
        CarbonLedger entity = new CarbonLedger(
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
    public Optional<CarbonLedger> findCarbonLedgerById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CarbonLedger processOptimization(String id, String tenantId) {
        CarbonLedger existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CarbonLedger optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
