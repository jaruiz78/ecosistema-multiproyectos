package com.corp.proyectogroundwater3daquifermodel.infrastructure.adapter.out.persistence;

import com.corp.proyectogroundwater3daquifermodel.domain.model.AquiferHydraulicHeadPiezometerNode;
import com.corp.proyectogroundwater3daquifermodel.domain.port.out.AquiferHydraulicHeadPiezometerNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAquiferHydraulicHeadPiezometerNodeRepositoryAdapter implements AquiferHydraulicHeadPiezometerNodeRepositoryPort {

    private final ConcurrentMap<String, AquiferHydraulicHeadPiezometerNode> storage = new ConcurrentHashMap<>();

    @Override
    public AquiferHydraulicHeadPiezometerNode save(AquiferHydraulicHeadPiezometerNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AquiferHydraulicHeadPiezometerNode> findById(String id, String tenantId) {
        AquiferHydraulicHeadPiezometerNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
