package com.corp.proyectotaxcomplianceledger.infrastructure.adapter.out.persistence;

import com.corp.proyectotaxcomplianceledger.domain.model.TaxComplianceLedger;
import com.corp.proyectotaxcomplianceledger.domain.port.out.TaxComplianceLedgerRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_sistemas_distribuidos">FACULTAD_II: Sistemas Distribuidos, Consenso & TLA+</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class InMemoryTaxComplianceLedgerRepositoryAdapter implements TaxComplianceLedgerRepositoryPort {

    private final ConcurrentMap<String, TaxComplianceLedger> storage = new ConcurrentHashMap<>();

    @Override
    public TaxComplianceLedger save(TaxComplianceLedger entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<TaxComplianceLedger> findById(String id, String tenantId) {
        TaxComplianceLedger entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
