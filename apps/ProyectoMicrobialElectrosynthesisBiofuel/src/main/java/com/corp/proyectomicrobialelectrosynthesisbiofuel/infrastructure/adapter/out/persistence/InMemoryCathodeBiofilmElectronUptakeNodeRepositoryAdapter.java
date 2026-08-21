package com.corp.proyectomicrobialelectrosynthesisbiofuel.infrastructure.adapter.out.persistence;

import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.model.CathodeBiofilmElectronUptakeNode;
import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.port.out.CathodeBiofilmElectronUptakeNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryCathodeBiofilmElectronUptakeNodeRepositoryAdapter implements CathodeBiofilmElectronUptakeNodeRepositoryPort {

    private final ConcurrentMap<String, CathodeBiofilmElectronUptakeNode> storage = new ConcurrentHashMap<>();

    @Override
    public CathodeBiofilmElectronUptakeNode save(CathodeBiofilmElectronUptakeNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CathodeBiofilmElectronUptakeNode> findById(String id, String tenantId) {
        CathodeBiofilmElectronUptakeNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
