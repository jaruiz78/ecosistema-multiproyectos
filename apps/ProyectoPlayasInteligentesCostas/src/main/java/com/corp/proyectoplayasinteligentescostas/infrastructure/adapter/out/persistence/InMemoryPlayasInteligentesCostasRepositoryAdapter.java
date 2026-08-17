package com.corp.proyectoplayasinteligentescostas.infrastructure.adapter.out.persistence;

import com.corp.proyectoplayasinteligentescostas.domain.model.PlayasInteligentesCostas;
import com.corp.proyectoplayasinteligentescostas.domain.port.out.PlayasInteligentesCostasRepositoryPort;
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
public class InMemoryPlayasInteligentesCostasRepositoryAdapter implements PlayasInteligentesCostasRepositoryPort {

    private final ConcurrentMap<String, PlayasInteligentesCostas> storage = new ConcurrentHashMap<>();

    @Override
    public PlayasInteligentesCostas save(PlayasInteligentesCostas entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PlayasInteligentesCostas> findById(String id, String tenantId) {
        PlayasInteligentesCostas entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
