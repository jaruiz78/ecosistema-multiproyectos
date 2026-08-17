package com.corp.proyectov2g.infrastructure.adapter.out.persistence;

import com.corp.proyectov2g.domain.model.V2G;
import com.corp.proyectov2g.domain.port.out.V2GRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class InMemoryV2GRepositoryAdapter implements V2GRepositoryPort {

    private final ConcurrentMap<String, V2G> storage = new ConcurrentHashMap<>();

    @Override
    public V2G save(V2G entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<V2G> findById(String id, String tenantId) {
        V2G entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
