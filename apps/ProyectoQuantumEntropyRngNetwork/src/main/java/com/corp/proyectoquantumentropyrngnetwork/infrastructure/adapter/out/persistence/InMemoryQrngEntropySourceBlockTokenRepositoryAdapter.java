package com.corp.proyectoquantumentropyrngnetwork.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantumentropyrngnetwork.domain.model.QrngEntropySourceBlockToken;
import com.corp.proyectoquantumentropyrngnetwork.domain.port.out.QrngEntropySourceBlockTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryQrngEntropySourceBlockTokenRepositoryAdapter implements QrngEntropySourceBlockTokenRepositoryPort {

    private final ConcurrentMap<String, QrngEntropySourceBlockToken> storage = new ConcurrentHashMap<>();

    @Override
    public QrngEntropySourceBlockToken save(QrngEntropySourceBlockToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<QrngEntropySourceBlockToken> findById(String id, String tenantId) {
        QrngEntropySourceBlockToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
