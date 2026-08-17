package com.corp.proyectonuclearfusionstellarator.infrastructure.adapter;

import com.corp.proyectonuclearfusionstellarator.application.port.out.StellaratorRepositoryPort;
import com.corp.proyectonuclearfusionstellarator.domain.StellaratorMagneticField;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryStellaratorRepositoryAdapter implements StellaratorRepositoryPort {

    private final Map<String, StellaratorMagneticField> store = new ConcurrentHashMap<>();

    @Override
    public void save(StellaratorMagneticField field) {
        store.put(field.reactorId(), field);
    }

    @Override
    public Optional<StellaratorMagneticField> findById(String reactorId) {
        return Optional.ofNullable(store.get(reactorId));
    }
}
