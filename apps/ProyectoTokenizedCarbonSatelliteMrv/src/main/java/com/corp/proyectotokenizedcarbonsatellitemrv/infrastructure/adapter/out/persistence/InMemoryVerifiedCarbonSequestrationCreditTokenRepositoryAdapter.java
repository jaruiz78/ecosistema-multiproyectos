package com.corp.proyectotokenizedcarbonsatellitemrv.infrastructure.adapter.out.persistence;

import com.corp.proyectotokenizedcarbonsatellitemrv.domain.model.VerifiedCarbonSequestrationCreditToken;
import com.corp.proyectotokenizedcarbonsatellitemrv.domain.port.out.VerifiedCarbonSequestrationCreditTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryVerifiedCarbonSequestrationCreditTokenRepositoryAdapter implements VerifiedCarbonSequestrationCreditTokenRepositoryPort {

    private final ConcurrentMap<String, VerifiedCarbonSequestrationCreditToken> storage = new ConcurrentHashMap<>();

    @Override
    public VerifiedCarbonSequestrationCreditToken save(VerifiedCarbonSequestrationCreditToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<VerifiedCarbonSequestrationCreditToken> findById(String id, String tenantId) {
        VerifiedCarbonSequestrationCreditToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
