package com.corp.proyectobluecarbonoceans.infrastructure.adapter.out.persistence;

import com.corp.proyectobluecarbonoceans.domain.model.MarinePosidoniaCarbonSink;
import com.corp.proyectobluecarbonoceans.domain.port.out.MarinePosidoniaCarbonSinkRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryMarinePosidoniaCarbonSinkRepositoryAdapter implements MarinePosidoniaCarbonSinkRepositoryPort {

    private final ConcurrentMap<String, MarinePosidoniaCarbonSink> storage = new ConcurrentHashMap<>();

    @Override
    public MarinePosidoniaCarbonSink save(MarinePosidoniaCarbonSink entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MarinePosidoniaCarbonSink> findById(String id, String tenantId) {
        MarinePosidoniaCarbonSink entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
