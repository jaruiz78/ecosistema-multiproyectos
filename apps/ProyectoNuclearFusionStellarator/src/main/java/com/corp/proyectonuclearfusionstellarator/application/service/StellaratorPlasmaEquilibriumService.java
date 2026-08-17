package com.corp.proyectonuclearfusionstellarator.application.service;

import com.corp.proyectonuclearfusionstellarator.application.port.out.StellaratorRepositoryPort;
import com.corp.proyectonuclearfusionstellarator.domain.StellaratorMagneticField;
import org.springframework.stereotype.Service;

@Service
public class StellaratorPlasmaEquilibriumService {

    private final StellaratorRepositoryPort repositoryPort;

    public StellaratorPlasmaEquilibriumService(StellaratorRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public StellaratorMagneticField optimizeMagneticGeometry(String reactorId, int coils, double tesla, double iota) {
        var field = StellaratorMagneticField.create(reactorId, coils, tesla, iota);
        repositoryPort.save(field);
        return field;
    }
}
