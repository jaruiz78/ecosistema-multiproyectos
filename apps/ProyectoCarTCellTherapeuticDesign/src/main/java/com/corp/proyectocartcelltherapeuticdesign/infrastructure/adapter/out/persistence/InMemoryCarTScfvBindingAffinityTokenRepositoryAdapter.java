package com.corp.proyectocartcelltherapeuticdesign.infrastructure.adapter.out.persistence;

import com.corp.proyectocartcelltherapeuticdesign.domain.model.CarTScfvBindingAffinityToken;
import com.corp.proyectocartcelltherapeuticdesign.domain.port.out.CarTScfvBindingAffinityTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryCarTScfvBindingAffinityTokenRepositoryAdapter implements CarTScfvBindingAffinityTokenRepositoryPort {

    private final ConcurrentMap<String, CarTScfvBindingAffinityToken> storage = new ConcurrentHashMap<>();

    @Override
    public CarTScfvBindingAffinityToken save(CarTScfvBindingAffinityToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CarTScfvBindingAffinityToken> findById(String id, String tenantId) {
        CarTScfvBindingAffinityToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
