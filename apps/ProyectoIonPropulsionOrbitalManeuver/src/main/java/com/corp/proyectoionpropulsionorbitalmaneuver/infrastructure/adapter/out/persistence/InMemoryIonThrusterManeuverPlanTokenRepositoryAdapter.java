package com.corp.proyectoionpropulsionorbitalmaneuver.infrastructure.adapter.out.persistence;

import com.corp.proyectoionpropulsionorbitalmaneuver.domain.model.IonThrusterManeuverPlanToken;
import com.corp.proyectoionpropulsionorbitalmaneuver.domain.port.out.IonThrusterManeuverPlanTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
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
