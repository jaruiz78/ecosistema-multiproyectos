package com.corp.proyectoaavvectortherapeuticdesign.infrastructure.adapter.out.persistence;

import com.corp.proyectoaavvectortherapeuticdesign.domain.model.AavCapsidTropismVectorToken;
import com.corp.proyectoaavvectortherapeuticdesign.domain.port.out.AavCapsidTropismVectorTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAavCapsidTropismVectorTokenRepositoryAdapter implements AavCapsidTropismVectorTokenRepositoryPort {

    private final ConcurrentMap<String, AavCapsidTropismVectorToken> storage = new ConcurrentHashMap<>();

    @Override
    public AavCapsidTropismVectorToken save(AavCapsidTropismVectorToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AavCapsidTropismVectorToken> findById(String id, String tenantId) {
        AavCapsidTropismVectorToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
