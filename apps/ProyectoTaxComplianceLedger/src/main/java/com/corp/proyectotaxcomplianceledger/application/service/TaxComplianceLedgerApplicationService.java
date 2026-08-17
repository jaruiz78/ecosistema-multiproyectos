package com.corp.proyectotaxcomplianceledger.application.service;

import com.corp.proyectotaxcomplianceledger.domain.model.TaxComplianceLedger;
import com.corp.proyectotaxcomplianceledger.domain.port.in.ManageTaxComplianceLedgerUseCase;
import com.corp.proyectotaxcomplianceledger.domain.port.out.TaxComplianceLedgerRepositoryPort;
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
public class TaxComplianceLedgerApplicationService implements ManageTaxComplianceLedgerUseCase {

    private final TaxComplianceLedgerRepositoryPort repositoryPort;

    public TaxComplianceLedgerApplicationService(TaxComplianceLedgerRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public TaxComplianceLedger createTaxComplianceLedger(String tenantId, String title, double value) {
        TaxComplianceLedger entity = new TaxComplianceLedger(
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
    public Optional<TaxComplianceLedger> findTaxComplianceLedgerById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public TaxComplianceLedger processOptimization(String id, String tenantId) {
        TaxComplianceLedger existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        TaxComplianceLedger optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
