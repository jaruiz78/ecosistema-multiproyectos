package com.corp.proyectofractionalrealestaterwa.infrastructure.adapter.out.persistence;

import com.corp.proyectofractionalrealestaterwa.domain.model.RealEstateNotarizedTitleToken;
import com.corp.proyectofractionalrealestaterwa.domain.port.out.RealEstateNotarizedTitleTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryRealEstateNotarizedTitleTokenRepositoryAdapter implements RealEstateNotarizedTitleTokenRepositoryPort {

    private final ConcurrentMap<String, RealEstateNotarizedTitleToken> storage = new ConcurrentHashMap<>();

    @Override
    public RealEstateNotarizedTitleToken save(RealEstateNotarizedTitleToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<RealEstateNotarizedTitleToken> findById(String id, String tenantId) {
        RealEstateNotarizedTitleToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
