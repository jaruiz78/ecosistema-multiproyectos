package com.corp.proyectoelectronicbillofladingepcis.infrastructure.adapter.out.persistence;

import com.corp.proyectoelectronicbillofladingepcis.domain.model.EpcisShippingEventRecordToken;
import com.corp.proyectoelectronicbillofladingepcis.domain.port.out.EpcisShippingEventRecordTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryEpcisShippingEventRecordTokenRepositoryAdapter implements EpcisShippingEventRecordTokenRepositoryPort {

    private final ConcurrentMap<String, EpcisShippingEventRecordToken> storage = new ConcurrentHashMap<>();

    @Override
    public EpcisShippingEventRecordToken save(EpcisShippingEventRecordToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EpcisShippingEventRecordToken> findById(String id, String tenantId) {
        EpcisShippingEventRecordToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
