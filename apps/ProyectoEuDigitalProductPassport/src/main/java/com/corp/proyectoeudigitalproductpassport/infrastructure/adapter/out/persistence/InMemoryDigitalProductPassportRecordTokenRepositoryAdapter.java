package com.corp.proyectoeudigitalproductpassport.infrastructure.adapter.out.persistence;

import com.corp.proyectoeudigitalproductpassport.domain.model.DigitalProductPassportRecordToken;
import com.corp.proyectoeudigitalproductpassport.domain.port.out.DigitalProductPassportRecordTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryDigitalProductPassportRecordTokenRepositoryAdapter implements DigitalProductPassportRecordTokenRepositoryPort {

    private final ConcurrentMap<String, DigitalProductPassportRecordToken> storage = new ConcurrentHashMap<>();

    @Override
    public DigitalProductPassportRecordToken save(DigitalProductPassportRecordToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DigitalProductPassportRecordToken> findById(String id, String tenantId) {
        DigitalProductPassportRecordToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
