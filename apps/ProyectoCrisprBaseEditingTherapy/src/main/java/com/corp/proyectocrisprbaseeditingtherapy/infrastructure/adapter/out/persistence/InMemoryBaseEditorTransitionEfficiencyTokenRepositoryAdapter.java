package com.corp.proyectocrisprbaseeditingtherapy.infrastructure.adapter.out.persistence;

import com.corp.proyectocrisprbaseeditingtherapy.domain.model.BaseEditorTransitionEfficiencyToken;
import com.corp.proyectocrisprbaseeditingtherapy.domain.port.out.BaseEditorTransitionEfficiencyTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryBaseEditorTransitionEfficiencyTokenRepositoryAdapter implements BaseEditorTransitionEfficiencyTokenRepositoryPort {

    private final ConcurrentMap<String, BaseEditorTransitionEfficiencyToken> storage = new ConcurrentHashMap<>();

    @Override
    public BaseEditorTransitionEfficiencyToken save(BaseEditorTransitionEfficiencyToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BaseEditorTransitionEfficiencyToken> findById(String id, String tenantId) {
        BaseEditorTransitionEfficiencyToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
