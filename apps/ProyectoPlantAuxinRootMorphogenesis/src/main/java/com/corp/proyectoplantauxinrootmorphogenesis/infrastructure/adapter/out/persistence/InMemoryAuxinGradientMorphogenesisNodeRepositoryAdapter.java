package com.corp.proyectoplantauxinrootmorphogenesis.infrastructure.adapter.out.persistence;

import com.corp.proyectoplantauxinrootmorphogenesis.domain.model.AuxinGradientMorphogenesisNode;
import com.corp.proyectoplantauxinrootmorphogenesis.domain.port.out.AuxinGradientMorphogenesisNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAuxinGradientMorphogenesisNodeRepositoryAdapter implements AuxinGradientMorphogenesisNodeRepositoryPort {

    private final ConcurrentMap<String, AuxinGradientMorphogenesisNode> storage = new ConcurrentHashMap<>();

    @Override
    public AuxinGradientMorphogenesisNode save(AuxinGradientMorphogenesisNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AuxinGradientMorphogenesisNode> findById(String id, String tenantId) {
        AuxinGradientMorphogenesisNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
