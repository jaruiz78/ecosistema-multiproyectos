package com.corp.proyectoalgalbiodieselrefinery.infrastructure.adapter.out.persistence;

import com.corp.proyectoalgalbiodieselrefinery.domain.model.LipidToBiodieselYieldConversionToken;
import com.corp.proyectoalgalbiodieselrefinery.domain.port.out.LipidToBiodieselYieldConversionTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryLipidToBiodieselYieldConversionTokenRepositoryAdapter implements LipidToBiodieselYieldConversionTokenRepositoryPort {

    private final ConcurrentMap<String, LipidToBiodieselYieldConversionToken> storage = new ConcurrentHashMap<>();

    @Override
    public LipidToBiodieselYieldConversionToken save(LipidToBiodieselYieldConversionToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<LipidToBiodieselYieldConversionToken> findById(String id, String tenantId) {
        LipidToBiodieselYieldConversionToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
