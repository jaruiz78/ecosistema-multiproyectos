package com.corp.proyectosyntheticenzymebiofoundry.domain.port.out;

import com.corp.proyectosyntheticenzymebiofoundry.domain.model.SyntheticEnzymeSequence;
import java.util.Optional;

public interface EnzymeRepositoryPort {
    SyntheticEnzymeSequence save(SyntheticEnzymeSequence sequence);
    Optional<SyntheticEnzymeSequence> findById(String enzymeDesignId);
}
