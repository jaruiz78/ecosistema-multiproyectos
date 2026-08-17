package com.corp.proyectonuclearfusionstellarator.application.port.out;

import com.corp.proyectonuclearfusionstellarator.domain.StellaratorMagneticField;
import java.util.Optional;

public interface StellaratorRepositoryPort {
    void save(StellaratorMagneticField field);
    Optional<StellaratorMagneticField> findById(String reactorId);
}
