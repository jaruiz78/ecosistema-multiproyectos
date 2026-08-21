package com.corp.proyectoplasmonicsurfacebiosensors.infrastructure.adapter.out.persistence;

import com.corp.proyectoplasmonicsurfacebiosensors.domain.model.PlasmonicResonanceShiftToken;
import com.corp.proyectoplasmonicsurfacebiosensors.domain.port.out.PlasmonicResonanceShiftTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryPlasmonicResonanceShiftTokenRepositoryAdapter implements PlasmonicResonanceShiftTokenRepositoryPort {

    private final ConcurrentMap<String, PlasmonicResonanceShiftToken> storage = new ConcurrentHashMap<>();

    @Override
    public PlasmonicResonanceShiftToken save(PlasmonicResonanceShiftToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PlasmonicResonanceShiftToken> findById(String id, String tenantId) {
        PlasmonicResonanceShiftToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
