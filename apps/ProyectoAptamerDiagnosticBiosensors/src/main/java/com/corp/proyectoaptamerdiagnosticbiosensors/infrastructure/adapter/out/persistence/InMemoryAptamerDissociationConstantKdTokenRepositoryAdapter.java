package com.corp.proyectoaptamerdiagnosticbiosensors.infrastructure.adapter.out.persistence;

import com.corp.proyectoaptamerdiagnosticbiosensors.domain.model.AptamerDissociationConstantKdToken;
import com.corp.proyectoaptamerdiagnosticbiosensors.domain.port.out.AptamerDissociationConstantKdTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAptamerDissociationConstantKdTokenRepositoryAdapter implements AptamerDissociationConstantKdTokenRepositoryPort {

    private final ConcurrentMap<String, AptamerDissociationConstantKdToken> storage = new ConcurrentHashMap<>();

    @Override
    public AptamerDissociationConstantKdToken save(AptamerDissociationConstantKdToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AptamerDissociationConstantKdToken> findById(String id, String tenantId) {
        AptamerDissociationConstantKdToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
