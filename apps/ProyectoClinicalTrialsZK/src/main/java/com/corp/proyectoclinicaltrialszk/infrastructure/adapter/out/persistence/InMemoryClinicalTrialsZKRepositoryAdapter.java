package com.corp.proyectoclinicaltrialszk.infrastructure.adapter.out.persistence;

import com.corp.proyectoclinicaltrialszk.domain.model.ClinicalTrialsZK;
import com.corp.proyectoclinicaltrialszk.domain.port.out.ClinicalTrialsZKRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class InMemoryClinicalTrialsZKRepositoryAdapter implements ClinicalTrialsZKRepositoryPort {

    private final ConcurrentMap<String, ClinicalTrialsZK> storage = new ConcurrentHashMap<>();

    @Override
    public ClinicalTrialsZK save(ClinicalTrialsZK entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ClinicalTrialsZK> findById(String id, String tenantId) {
        ClinicalTrialsZK entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
