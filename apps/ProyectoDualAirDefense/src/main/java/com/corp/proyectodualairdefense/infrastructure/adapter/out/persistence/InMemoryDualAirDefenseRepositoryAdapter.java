package com.corp.proyectodualairdefense.infrastructure.adapter.out.persistence;

import com.corp.proyectodualairdefense.domain.model.DualAirDefense;
import com.corp.proyectodualairdefense.domain.port.out.DualAirDefenseRepositoryPort;
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
public class InMemoryDualAirDefenseRepositoryAdapter implements DualAirDefenseRepositoryPort {

    private final ConcurrentMap<String, DualAirDefense> storage = new ConcurrentHashMap<>();

    @Override
    public DualAirDefense save(DualAirDefense entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DualAirDefense> findById(String id, String tenantId) {
        DualAirDefense entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
