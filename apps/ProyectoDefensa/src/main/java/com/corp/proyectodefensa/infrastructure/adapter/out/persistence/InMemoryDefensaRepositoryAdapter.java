package com.corp.proyectodefensa.infrastructure.adapter.out.persistence;

import com.corp.proyectodefensa.domain.model.Defensa;
import com.corp.proyectodefensa.domain.port.out.DefensaRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_10_identidad_soberana_privacidad_zkp">FACULTAD_XI: Identidad Soberana & Zero-Trust BeyondCorp</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class InMemoryDefensaRepositoryAdapter implements DefensaRepositoryPort {

    private final ConcurrentMap<String, Defensa> storage = new ConcurrentHashMap<>();

    @Override
    public Defensa save(Defensa entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<Defensa> findById(String id, String tenantId) {
        Defensa entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
