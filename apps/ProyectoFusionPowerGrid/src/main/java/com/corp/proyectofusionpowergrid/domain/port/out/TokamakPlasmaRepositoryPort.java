package com.corp.proyectofusionpowergrid.domain.port.out;

import com.corp.proyectofusionpowergrid.domain.model.TokamakPlasmaState;
import java.util.Optional;

public interface TokamakPlasmaRepositoryPort {
    TokamakPlasmaState save(TokamakPlasmaState state);
    Optional<TokamakPlasmaState> findById(String reactorId);
}
