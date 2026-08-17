package com.corp.proyectodenovoplasticdegradation.application.port.out;

import com.corp.proyectodenovoplasticdegradation.domain.PolymerDegradationEnzyme;
import java.util.Optional;

public interface PolymerEnzymeRepositoryPort {
    void save(PolymerDegradationEnzyme enzyme);
    Optional<PolymerDegradationEnzyme> findById(String enzymeId);
}
