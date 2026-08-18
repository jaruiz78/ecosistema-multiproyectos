package com.corp.proyectopostquantumcertificateauthority.infrastructure.adapter.out.persistence;

import com.corp.proyectopostquantumcertificateauthority.domain.model.PqHybridX509CertificateToken;
import com.corp.proyectopostquantumcertificateauthority.domain.port.out.PqHybridX509CertificateTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryPqHybridX509CertificateTokenRepositoryAdapter implements PqHybridX509CertificateTokenRepositoryPort {

    private final ConcurrentMap<String, PqHybridX509CertificateToken> storage = new ConcurrentHashMap<>();

    @Override
    public PqHybridX509CertificateToken save(PqHybridX509CertificateToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PqHybridX509CertificateToken> findById(String id, String tenantId) {
        PqHybridX509CertificateToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
