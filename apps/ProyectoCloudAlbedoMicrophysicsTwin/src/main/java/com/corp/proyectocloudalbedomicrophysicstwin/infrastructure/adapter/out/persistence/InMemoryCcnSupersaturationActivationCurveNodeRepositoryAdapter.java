package com.corp.proyectocloudalbedomicrophysicstwin.infrastructure.adapter.out.persistence;

import com.corp.proyectocloudalbedomicrophysicstwin.domain.model.CcnSupersaturationActivationCurveNode;
import com.corp.proyectocloudalbedomicrophysicstwin.domain.port.out.CcnSupersaturationActivationCurveNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryCcnSupersaturationActivationCurveNodeRepositoryAdapter implements CcnSupersaturationActivationCurveNodeRepositoryPort {

    private final ConcurrentMap<String, CcnSupersaturationActivationCurveNode> storage = new ConcurrentHashMap<>();

    @Override
    public CcnSupersaturationActivationCurveNode save(CcnSupersaturationActivationCurveNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CcnSupersaturationActivationCurveNode> findById(String id, String tenantId) {
        CcnSupersaturationActivationCurveNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
