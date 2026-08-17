package com.corp.proyectobiodiversitygenomicbank.domain.port.out;

import com.corp.proyectobiodiversitygenomicbank.domain.model.EnvironmentalDnaSample;
import java.util.Optional;

public interface EdnaSampleRepositoryPort {
    EnvironmentalDnaSample save(EnvironmentalDnaSample sample);
    Optional<EnvironmentalDnaSample> findById(String sampleId);
}
