package com.corp.proyectoparquesnacionalesnatura2000.infrastructure.adapter.out.persistence;

import com.corp.proyectoparquesnacionalesnatura2000.domain.model.ParquesNacionalesNatura2000;
import com.corp.proyectoparquesnacionalesnatura2000.domain.port.out.ParquesNacionalesNatura2000RepositoryPort;
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
public class InMemoryParquesNacionalesNatura2000RepositoryAdapter implements ParquesNacionalesNatura2000RepositoryPort {

    private final ConcurrentMap<String, ParquesNacionalesNatura2000> storage = new ConcurrentHashMap<>();

    @Override
    public ParquesNacionalesNatura2000 save(ParquesNacionalesNatura2000 entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ParquesNacionalesNatura2000> findById(String id, String tenantId) {
        ParquesNacionalesNatura2000 entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
