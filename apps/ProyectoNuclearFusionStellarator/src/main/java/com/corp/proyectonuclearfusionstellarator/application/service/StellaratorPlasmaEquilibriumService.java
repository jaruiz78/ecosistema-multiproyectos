package com.corp.proyectonuclearfusionstellarator.application.service;

import com.corp.proyectonuclearfusionstellarator.application.port.out.StellaratorRepositoryPort;
import com.corp.proyectonuclearfusionstellarator.domain.StellaratorMagneticField;
import org.springframework.stereotype.Service;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
