package com.corp.proyectobiodiversitygenomicbank.infrastructure.adapter.out.persistence;

import com.corp.proyectobiodiversitygenomicbank.domain.model.EnvironmentalDnaSample;
import com.corp.proyectobiodiversitygenomicbank.domain.port.out.EdnaSampleRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
