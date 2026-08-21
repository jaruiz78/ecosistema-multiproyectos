package com.corp.proyectoxenotransplantationimmunetwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoxenotransplantationimmunetwin.domain.model.XenoOrganCompatibilityScoreToken;
import com.corp.proyectoxenotransplantationimmunetwin.domain.port.out.XenoOrganCompatibilityScoreTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
