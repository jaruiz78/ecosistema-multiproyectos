package com.corp.proyectocarbonledger.infrastructure.adapter.out.persistence;

import com.corp.proyectocarbonledger.domain.model.CarbonLedger;
import com.corp.proyectocarbonledger.domain.port.out.CarbonLedgerRepositoryPort;
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
public class InMemoryCarbonLedgerRepositoryAdapter implements CarbonLedgerRepositoryPort {

    private final ConcurrentMap<String, CarbonLedger> storage = new ConcurrentHashMap<>();

    @Override
    public CarbonLedger save(CarbonLedger entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CarbonLedger> findById(String id, String tenantId) {
        CarbonLedger entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
