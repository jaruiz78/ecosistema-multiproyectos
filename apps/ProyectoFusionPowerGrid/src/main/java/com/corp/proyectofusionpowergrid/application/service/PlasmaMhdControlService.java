package com.corp.proyectofusionpowergrid.application.service;

import com.corp.proyectofusionpowergrid.domain.model.TokamakPlasmaState;
import com.corp.proyectofusionpowergrid.domain.port.out.TokamakPlasmaRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PlasmaMhdControlService {

    private final TokamakPlasmaRepositoryPort repositoryPort;

    public PlasmaMhdControlService(TokamakPlasmaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public TokamakPlasmaState regulatePlasmaStability(String reactorId, double coreTempKeV, double measuredBeta) {
        TokamakPlasmaState state = repositoryPort.findById(reactorId)
                .orElseGet(() -> TokamakPlasmaState.create(reactorId, 5.3));

        TokamakPlasmaState updated = state.updateMhdParameters(coreTempKeV, measuredBeta);
        return repositoryPort.save(updated);
    }
}
