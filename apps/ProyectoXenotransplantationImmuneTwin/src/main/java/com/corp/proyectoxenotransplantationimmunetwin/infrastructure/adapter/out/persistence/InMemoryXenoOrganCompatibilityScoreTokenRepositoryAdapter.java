package com.corp.proyectoxenotransplantationimmunetwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoxenotransplantationimmunetwin.domain.model.XenoOrganCompatibilityScoreToken;
import com.corp.proyectoxenotransplantationimmunetwin.domain.port.out.XenoOrganCompatibilityScoreTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryXenoOrganCompatibilityScoreTokenRepositoryAdapter implements XenoOrganCompatibilityScoreTokenRepositoryPort {

    private final ConcurrentMap<String, XenoOrganCompatibilityScoreToken> storage = new ConcurrentHashMap<>();

    @Override
    public XenoOrganCompatibilityScoreToken save(XenoOrganCompatibilityScoreToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<XenoOrganCompatibilityScoreToken> findById(String id, String tenantId) {
        XenoOrganCompatibilityScoreToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
