package com.corp.proyectoicecorepaleoclimatearchive.infrastructure.adapter.out.persistence;

import com.corp.proyectoicecorepaleoclimatearchive.domain.model.IsotopicDeltaO18TemperatureProfileToken;
import com.corp.proyectoicecorepaleoclimatearchive.domain.port.out.IsotopicDeltaO18TemperatureProfileTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryIsotopicDeltaO18TemperatureProfileTokenRepositoryAdapter implements IsotopicDeltaO18TemperatureProfileTokenRepositoryPort {

    private final ConcurrentMap<String, IsotopicDeltaO18TemperatureProfileToken> storage = new ConcurrentHashMap<>();

    @Override
    public IsotopicDeltaO18TemperatureProfileToken save(IsotopicDeltaO18TemperatureProfileToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<IsotopicDeltaO18TemperatureProfileToken> findById(String id, String tenantId) {
        IsotopicDeltaO18TemperatureProfileToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
