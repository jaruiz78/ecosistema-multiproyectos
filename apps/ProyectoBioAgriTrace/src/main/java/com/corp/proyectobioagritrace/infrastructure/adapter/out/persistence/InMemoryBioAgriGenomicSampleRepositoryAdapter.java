package com.corp.proyectobioagritrace.infrastructure.adapter.out.persistence;

import com.corp.proyectobioagritrace.domain.model.BioAgriGenomicSample;
import com.corp.proyectobioagritrace.domain.port.out.BioAgriGenomicSampleRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryBioAgriGenomicSampleRepositoryAdapter implements BioAgriGenomicSampleRepositoryPort {

    private final ConcurrentMap<String, BioAgriGenomicSample> storage = new ConcurrentHashMap<>();

    @Override
    public BioAgriGenomicSample save(BioAgriGenomicSample entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BioAgriGenomicSample> findById(String id, String tenantId) {
        BioAgriGenomicSample entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
