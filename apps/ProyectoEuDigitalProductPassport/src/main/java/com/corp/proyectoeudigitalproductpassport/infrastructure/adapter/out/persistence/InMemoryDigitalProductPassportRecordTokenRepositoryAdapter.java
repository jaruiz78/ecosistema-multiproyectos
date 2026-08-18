package com.corp.proyectoeudigitalproductpassport.infrastructure.adapter.out.persistence;

import com.corp.proyectoeudigitalproductpassport.domain.model.DigitalProductPassportRecordToken;
import com.corp.proyectoeudigitalproductpassport.domain.port.out.DigitalProductPassportRecordTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
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
