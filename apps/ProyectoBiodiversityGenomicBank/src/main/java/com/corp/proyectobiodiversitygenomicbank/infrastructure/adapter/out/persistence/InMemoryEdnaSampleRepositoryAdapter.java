package com.corp.proyectobiodiversitygenomicbank.infrastructure.adapter.out.persistence;

import com.corp.proyectobiodiversitygenomicbank.domain.model.EnvironmentalDnaSample;
import com.corp.proyectobiodiversitygenomicbank.domain.port.out.EdnaSampleRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryEdnaSampleRepositoryAdapter implements EdnaSampleRepositoryPort {

    private final Map<String, EnvironmentalDnaSample> bank = new ConcurrentHashMap<>();

    @Override
    public EnvironmentalDnaSample save(EnvironmentalDnaSample sample) {
        bank.put(sample.sampleId(), sample);
        return sample;
    }

    @Override
    public Optional<EnvironmentalDnaSample> findById(String sampleId) {
        return Optional.ofNullable(bank.get(sampleId));
    }
}
