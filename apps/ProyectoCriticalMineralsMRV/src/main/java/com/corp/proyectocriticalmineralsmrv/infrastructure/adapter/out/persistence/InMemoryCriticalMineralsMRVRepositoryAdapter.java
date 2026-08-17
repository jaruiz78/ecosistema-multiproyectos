package com.corp.proyectocriticalmineralsmrv.infrastructure.adapter.out.persistence;

import com.corp.proyectocriticalmineralsmrv.domain.model.CriticalMineralsMRV;
import com.corp.proyectocriticalmineralsmrv.domain.port.out.CriticalMineralsMRVRepositoryPort;
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
public class InMemoryCriticalMineralsMRVRepositoryAdapter implements CriticalMineralsMRVRepositoryPort {

    private final ConcurrentMap<String, CriticalMineralsMRV> storage = new ConcurrentHashMap<>();

    @Override
    public CriticalMineralsMRV save(CriticalMineralsMRV entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CriticalMineralsMRV> findById(String id, String tenantId) {
        CriticalMineralsMRV entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
