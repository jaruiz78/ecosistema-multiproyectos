package com.corp.proyectologistica.infrastructure.adapter.out.persistence;

import com.corp.proyectologistica.domain.model.Logistica;
import com.corp.proyectologistica.domain.port.out.LogisticaRepositoryPort;
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
public class InMemoryLogisticaRepositoryAdapter implements LogisticaRepositoryPort {

    private final ConcurrentMap<String, Logistica> storage = new ConcurrentHashMap<>();

    @Override
    public Logistica save(Logistica entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<Logistica> findById(String id, String tenantId) {
        Logistica entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
