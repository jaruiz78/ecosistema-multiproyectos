package com.corp.proyectozktaxcomplianceauditor.infrastructure.adapter.out.persistence;

import com.corp.proyectozktaxcomplianceauditor.domain.model.ZkTaxComplianceCertificateToken;
import com.corp.proyectozktaxcomplianceauditor.domain.port.out.ZkTaxComplianceCertificateTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryZkTaxComplianceCertificateTokenRepositoryAdapter implements ZkTaxComplianceCertificateTokenRepositoryPort {

    private final ConcurrentMap<String, ZkTaxComplianceCertificateToken> storage = new ConcurrentHashMap<>();

    @Override
    public ZkTaxComplianceCertificateToken save(ZkTaxComplianceCertificateToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ZkTaxComplianceCertificateToken> findById(String id, String tenantId) {
        ZkTaxComplianceCertificateToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
