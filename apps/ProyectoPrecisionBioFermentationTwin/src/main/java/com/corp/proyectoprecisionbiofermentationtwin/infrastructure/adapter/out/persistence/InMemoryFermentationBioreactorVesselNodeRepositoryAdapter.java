package com.corp.proyectoprecisionbiofermentationtwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoprecisionbiofermentationtwin.domain.model.FermentationBioreactorVesselNode;
import com.corp.proyectoprecisionbiofermentationtwin.domain.port.out.FermentationBioreactorVesselNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryFermentationBioreactorVesselNodeRepositoryAdapter implements FermentationBioreactorVesselNodeRepositoryPort {

    private final ConcurrentMap<String, FermentationBioreactorVesselNode> storage = new ConcurrentHashMap<>();

    @Override
    public FermentationBioreactorVesselNode save(FermentationBioreactorVesselNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<FermentationBioreactorVesselNode> findById(String id, String tenantId) {
        FermentationBioreactorVesselNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
