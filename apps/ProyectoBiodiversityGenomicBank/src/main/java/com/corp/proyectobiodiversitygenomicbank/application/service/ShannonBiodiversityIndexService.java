package com.corp.proyectobiodiversitygenomicbank.application.service;

import com.corp.proyectobiodiversitygenomicbank.domain.model.EnvironmentalDnaSample;
import com.corp.proyectobiodiversitygenomicbank.domain.port.out.EdnaSampleRepositoryPort;
import java.util.Map;

public class ShannonBiodiversityIndexService {

    private final EdnaSampleRepositoryPort repositoryPort;

    public ShannonBiodiversityIndexService(EdnaSampleRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public EnvironmentalDnaSample processMetagenomicReads(String sampleId, String biome, long h3, Map<String, Integer> speciesReads) {
        EnvironmentalDnaSample sample = EnvironmentalDnaSample.create(sampleId, biome, h3, speciesReads);
        return repositoryPort.save(sample);
    }
}
