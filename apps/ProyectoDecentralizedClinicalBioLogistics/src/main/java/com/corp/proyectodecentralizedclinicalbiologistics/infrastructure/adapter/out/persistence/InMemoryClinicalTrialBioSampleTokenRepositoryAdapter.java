package com.corp.proyectodecentralizedclinicalbiologistics.infrastructure.adapter.out.persistence;

import com.corp.proyectodecentralizedclinicalbiologistics.domain.model.ClinicalTrialBioSampleToken;
import com.corp.proyectodecentralizedclinicalbiologistics.domain.port.out.ClinicalTrialBioSampleTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryClinicalTrialBioSampleTokenRepositoryAdapter implements ClinicalTrialBioSampleTokenRepositoryPort {

    private final ConcurrentMap<String, ClinicalTrialBioSampleToken> storage = new ConcurrentHashMap<>();

    @Override
    public ClinicalTrialBioSampleToken save(ClinicalTrialBioSampleToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ClinicalTrialBioSampleToken> findById(String id, String tenantId) {
        ClinicalTrialBioSampleToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
