package com.corp.proyectomycorrhizalnetworkagronomy.infrastructure.adapter.out.persistence;

import com.corp.proyectomycorrhizalnetworkagronomy.domain.model.HyphalNutrientFluxTranslocationNode;
import com.corp.proyectomycorrhizalnetworkagronomy.domain.port.out.HyphalNutrientFluxTranslocationNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryHyphalNutrientFluxTranslocationNodeRepositoryAdapter implements HyphalNutrientFluxTranslocationNodeRepositoryPort {

    private final ConcurrentMap<String, HyphalNutrientFluxTranslocationNode> storage = new ConcurrentHashMap<>();

    @Override
    public HyphalNutrientFluxTranslocationNode save(HyphalNutrientFluxTranslocationNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HyphalNutrientFluxTranslocationNode> findById(String id, String tenantId) {
        HyphalNutrientFluxTranslocationNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
