package com.corp.proyectofusionpowergrid.domain.port.out;

import com.corp.proyectofusionpowergrid.domain.model.TokamakPlasmaState;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface TokamakPlasmaRepositoryPort {
    TokamakPlasmaState save(TokamakPlasmaState state);
    Optional<TokamakPlasmaState> findById(String reactorId);
}
