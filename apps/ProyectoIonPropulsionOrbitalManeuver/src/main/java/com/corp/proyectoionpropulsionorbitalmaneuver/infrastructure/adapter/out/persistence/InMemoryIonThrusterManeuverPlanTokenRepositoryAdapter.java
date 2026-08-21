package com.corp.proyectoionpropulsionorbitalmaneuver.infrastructure.adapter.out.persistence;

import com.corp.proyectoionpropulsionorbitalmaneuver.domain.model.IonThrusterManeuverPlanToken;
import com.corp.proyectoionpropulsionorbitalmaneuver.domain.port.out.IonThrusterManeuverPlanTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryIonThrusterManeuverPlanTokenRepositoryAdapter implements IonThrusterManeuverPlanTokenRepositoryPort {

    private final ConcurrentMap<String, IonThrusterManeuverPlanToken> storage = new ConcurrentHashMap<>();

    @Override
    public IonThrusterManeuverPlanToken save(IonThrusterManeuverPlanToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<IonThrusterManeuverPlanToken> findById(String id, String tenantId) {
        IonThrusterManeuverPlanToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
