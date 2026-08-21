package com.corp.proyectobiodiversitygenomicbank.application.service;

import com.corp.proyectobiodiversitygenomicbank.domain.model.EnvironmentalDnaSample;
import com.corp.proyectobiodiversitygenomicbank.domain.port.out.EdnaSampleRepositoryPort;
import java.util.Map;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
