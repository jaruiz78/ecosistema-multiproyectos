package com.corp.proyectoepigeneticbioagemonitor.infrastructure.adapter.out.persistence;

import com.corp.proyectoepigeneticbioagemonitor.domain.model.CpgMethylationProfileNode;
import com.corp.proyectoepigeneticbioagemonitor.domain.port.out.CpgMethylationProfileNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryCpgMethylationProfileNodeRepositoryAdapter implements CpgMethylationProfileNodeRepositoryPort {

    private final ConcurrentMap<String, CpgMethylationProfileNode> storage = new ConcurrentHashMap<>();

    @Override
    public CpgMethylationProfileNode save(CpgMethylationProfileNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CpgMethylationProfileNode> findById(String id, String tenantId) {
        CpgMethylationProfileNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
