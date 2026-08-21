package com.corp.proyectodenovoplasticdegradation.application.service;

import com.corp.proyectodenovoplasticdegradation.application.port.out.PolymerEnzymeRepositoryPort;
import com.corp.proyectodenovoplasticdegradation.domain.PolymerDegradationEnzyme;
import org.springframework.stereotype.Service;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PetPlasticDegradationService {

    private final PolymerEnzymeRepositoryPort repositoryPort;

    public PetPlasticDegradationService(PolymerEnzymeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public PolymerDegradationEnzyme engineerEnzyme(String enzymeId, String polymerType, double kcatKm, double tempCelsius) {
        var enzyme = PolymerDegradationEnzyme.create(enzymeId, polymerType, kcatKm, tempCelsius);
        repositoryPort.save(enzyme);
        return enzyme;
    }
}
