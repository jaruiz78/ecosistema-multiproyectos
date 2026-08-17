package com.corp.proyectosalud.infrastructure.adapter.out.persistence;

import com.corp.proyectosalud.domain.model.Salud;
import com.corp.proyectosalud.domain.port.out.SaludRepositoryPort;
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
public class InMemorySaludRepositoryAdapter implements SaludRepositoryPort {

    private final ConcurrentMap<String, Salud> storage = new ConcurrentHashMap<>();

    @Override
    public Salud save(Salud entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<Salud> findById(String id, String tenantId) {
        Salud entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
