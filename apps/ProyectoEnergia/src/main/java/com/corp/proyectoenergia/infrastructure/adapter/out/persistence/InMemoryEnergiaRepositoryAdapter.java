package com.corp.proyectoenergia.infrastructure.adapter.out.persistence;

import com.corp.proyectoenergia.domain.model.Energia;
import com.corp.proyectoenergia.domain.port.out.EnergiaRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion">FACULTAD_V: Gemelo Digital PEPS, EnKF & Física</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class InMemoryEnergiaRepositoryAdapter implements EnergiaRepositoryPort {

    private final ConcurrentMap<String, Energia> storage = new ConcurrentHashMap<>();

    @Override
    public Energia save(Energia entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<Energia> findById(String id, String tenantId) {
        Energia entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
