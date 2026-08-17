package com.corp.proyectocircular.infrastructure.adapter.out.persistence;

import com.corp.proyectocircular.domain.model.Circular;
import com.corp.proyectocircular.domain.port.out.CircularRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class InMemoryCircularRepositoryAdapter implements CircularRepositoryPort {

    private final ConcurrentMap<String, Circular> storage = new ConcurrentHashMap<>();

    @Override
    public Circular save(Circular entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<Circular> findById(String id, String tenantId) {
        Circular entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
