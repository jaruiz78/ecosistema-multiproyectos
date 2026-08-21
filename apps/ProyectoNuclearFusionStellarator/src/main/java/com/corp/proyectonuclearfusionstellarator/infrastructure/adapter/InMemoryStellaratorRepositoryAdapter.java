package com.corp.proyectonuclearfusionstellarator.infrastructure.adapter;

import com.corp.proyectonuclearfusionstellarator.application.port.out.StellaratorRepositoryPort;
import com.corp.proyectonuclearfusionstellarator.domain.StellaratorMagneticField;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
