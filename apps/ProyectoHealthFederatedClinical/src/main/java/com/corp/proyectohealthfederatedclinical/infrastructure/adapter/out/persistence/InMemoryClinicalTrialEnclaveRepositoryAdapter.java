package com.corp.proyectohealthfederatedclinical.infrastructure.adapter.out.persistence;

import com.corp.proyectohealthfederatedclinical.domain.model.ClinicalTrialEnclave;
import com.corp.proyectohealthfederatedclinical.domain.port.out.ClinicalTrialEnclaveRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryClinicalTrialEnclaveRepositoryAdapter implements ClinicalTrialEnclaveRepositoryPort {

    private final ConcurrentMap<String, ClinicalTrialEnclave> storage = new ConcurrentHashMap<>();

    @Override
    public ClinicalTrialEnclave save(ClinicalTrialEnclave entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ClinicalTrialEnclave> findById(String id, String tenantId) {
        ClinicalTrialEnclave entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
