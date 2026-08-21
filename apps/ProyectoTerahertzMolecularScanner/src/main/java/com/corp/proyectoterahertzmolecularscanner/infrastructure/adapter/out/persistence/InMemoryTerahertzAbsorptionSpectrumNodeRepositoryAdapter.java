package com.corp.proyectoterahertzmolecularscanner.infrastructure.adapter.out.persistence;

import com.corp.proyectoterahertzmolecularscanner.domain.model.TerahertzAbsorptionSpectrumNode;
import com.corp.proyectoterahertzmolecularscanner.domain.port.out.TerahertzAbsorptionSpectrumNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryTerahertzAbsorptionSpectrumNodeRepositoryAdapter implements TerahertzAbsorptionSpectrumNodeRepositoryPort {

    private final ConcurrentMap<String, TerahertzAbsorptionSpectrumNode> storage = new ConcurrentHashMap<>();

    @Override
    public TerahertzAbsorptionSpectrumNode save(TerahertzAbsorptionSpectrumNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<TerahertzAbsorptionSpectrumNode> findById(String id, String tenantId) {
        TerahertzAbsorptionSpectrumNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
