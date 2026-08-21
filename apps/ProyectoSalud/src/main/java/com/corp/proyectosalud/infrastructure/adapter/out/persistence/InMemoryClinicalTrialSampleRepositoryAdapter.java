package com.corp.proyectosalud.infrastructure.adapter.out.persistence;

import com.corp.proyectosalud.domain.model.ClinicalTrialSample;
import com.corp.proyectosalud.domain.port.out.ClinicalTrialSampleRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryClinicalTrialSampleRepositoryAdapter implements ClinicalTrialSampleRepositoryPort {

    private final ConcurrentMap<String, ClinicalTrialSample> storage = new ConcurrentHashMap<>();

    @Override
    public ClinicalTrialSample save(ClinicalTrialSample entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ClinicalTrialSample> findById(String id, String tenantId) {
        ClinicalTrialSample entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
